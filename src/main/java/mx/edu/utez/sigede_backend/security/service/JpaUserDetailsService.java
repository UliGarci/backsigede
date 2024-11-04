package mx.edu.utez.sigede_backend.security.service;

import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.utils.EncryptionUtil;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final UserAccountRepository repository;
    private final UserAccountRepository userAccountRepository;

    public JpaUserDetailsService(UserAccountRepository repository, UserAccountRepository userAccountRepository) {
        this.repository = repository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String encryptedEmail = EncryptionUtil.encrypt(email);

        //Optional<UserAccount> o = repository.getOneByEmail(encryptedEmail);
        UserAccount user = userAccountRepository.findByEmail(email);
        //if (!o.isPresent()) {
        if(user==null){
            throw new UsernameNotFoundException(String.format("El usuario %s no existe en el sistema", email));
        }

        //UserAccount user = o.orElseThrow();

        // Obtener el rol del usuario
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getFkRol().getName()));

        // Decodificar la contraseña (asumimos que es un hash bcrypt y no requiere desencriptación)
        String decodedPassword = new String(user.getPassword());

        return new User(
                email,
                decodedPassword,
                true,
                true,
                true,
                true,
                authorities
        );
    }
}
