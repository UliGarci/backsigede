package mx.edu.utez.sigede_backend.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.security.AuthenticationProcessingException;
import mx.edu.utez.sigede_backend.utils.EncryptionUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static mx.edu.utez.sigede_backend.security.TokenJwtConfig.*;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    EncryptionUtil encryptionUtil;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        setFilterProcessesUrl("/api/auth/login");
    }
    @Override
    public org.springframework.security.core.Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UserAccount user = null;
        String decryptedEmail = null;
        String password = null;
        try {
            // Obtener el objeto UserAccount desde la solicitud
            user = new ObjectMapper().readValue(request.getInputStream(), UserAccount.class);

            // Convertir el byte[] del email en una cadena Base64
            String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().getBytes(StandardCharsets.UTF_8));

            // Desencriptar el correo utilizando EncryptionUtil
            decryptedEmail = EncryptionUtil.decrypt(encodedEmail);
            password = new String(user.getPassword().getBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw new AuthenticationProcessingException("Error al procesar la autenticación", e);
        }
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(decryptedEmail, password);

        return authenticationManager.authenticate(authToken);
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String username= ((org.springframework.security.core.userdetails.User)authResult.getPrincipal())//estamos casteando el authResult a la clase User pero de SPring
                .getUsername();
        authResult.getAuthorities();//Esto obtiene el rol


        Claims claims = Jwts.claims();
        claims.put("authorities",new ObjectMapper().writeValueAsString(authResult.getAuthorities()));


        String token = Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .signWith(SECRET_KEY)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+3600000))
                .compact();
        response.addHeader(HEADER_AUTHORIZATION,PREFIX_TOKEN+token);

        Map<String,Object> body = new HashMap<>();
        body.put("token",token);
        body.put("message",String.format("Hola %s, has inciado sesion con exito",username));
        body.put("username",username);
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Map<String ,Object> body= new HashMap<>();
        body.put("messsage","Error en la autenticacion username o password es incorrecto");
        body.put("error",failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");

    }



}
