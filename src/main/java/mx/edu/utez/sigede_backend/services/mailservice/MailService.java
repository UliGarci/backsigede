package mx.edu.utez.sigede_backend.services.mailservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;
import mx.edu.utez.sigede_backend.models.user_account.UserAccountRepository;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCode;
import mx.edu.utez.sigede_backend.models.verification_code.VerificationCodeRepository;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VerificationCodeRepository verificationCodeRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void sendEmail(String email,String subject,String body)throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(body,true);
        mailSender.send(message);
    }

    @Transactional
    public void sendPasswordResetCode(String email)throws MessagingException{

        UserAccount userAccount = userAccountRepository.findByEmail(email);
        if(userAccount==null){
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
        }

        VerificationCode code = new VerificationCode();
        code.setVerificationCode(UUID.randomUUID().toString());
        code.setFkUserAccount(userAccount);
        code.setCreatedAt(LocalDateTime.now());
        code.setExpiration(LocalDateTime.now().plusMinutes(10));
        verificationCodeRepository.save(code);

        sendEmail(email,"Codigo de recuperación","" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Recuperación de contraseña</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            background-color: #EEEEEE;\n" +
                "            width: 400px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 5px;\n" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        img {\n" +
                "            max-width: 100%;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Recuperación de contraseña</h1>\n" +
                "        <img src=\"tu-imagen.jpg\" alt=\"Imagen de tu empresa\">\n" +
                "        <p>Te enviamos esta notificación para garantizar la privacidad y seguridad de tu cuenta de SIGEDE. Si has solicitado la recuperación de contraseña, este es tu código de verificación:</p>\n" +
                "        <p style=\"font-weight: bold\">"+code.getVerificationCode()+"</p>\n" +
                "        <p style=\"color: #888;\">Si no has solicitado este cambio, ignora este correo.</p>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");

    }

    @Transactional
    public void sendTemporaryPassword(String email) throws MessagingException{
        UserAccount newAdmin = userAccountRepository.findByEmail(email);

        if(newAdmin==null){
            throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
        }
        String temPasword = UUID.randomUUID().toString().substring(8);
        newAdmin.setPassword(temPasword);
        userAccountRepository.save(newAdmin);

        sendEmail(email,"Bienvenido Nuevo Administrador","" +
                "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Recuperación de contraseña</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            background-color: #f0f0f0;\n" +
                "        }\n" +
                "\n" +
                "        .container {\n" +
                "            background-color: #fff;\n" +
                "            width: 400px;\n" +
                "            margin: 0 auto;\n" +
                "            padding: 20px;\n" +
                "            border-radius: 30px;\n" +
                "            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "\n" +
                "        h1 {\n" +
                "            font-size: 24px;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "\n" +
                "        img {\n" +
                "            max-width: 100%;\n" +
                "            margin-bottom: 20px;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Bienvenido</h1>\n" +
                "        <img src=\"tu-imagen.jpg\" alt=\"Imagen de tu empresa\">\n" +
                "        <p>Hola bievenido a SIGEDE, te enviamos este correo para notificar tu registro exitoso como administrador, ya puedes iniciar sesión con tu correo electrónico con la siguiente contraseña temporal:</p>\n" +
                "        <p style=\"font-weight: bold\">"+temPasword+"</p>\n" +
                "<b>Recuerda cambiar tu cotraseña a una más segura</p>\n" +
                "        <p style=\"color: #888;\">Si no has solicitado este cambio, ignora este correo.</b>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>");
    }

    @Transactional
    public boolean validateResetCode(String email){
        try {
            UserAccount user = userAccountRepository.findByEmail(email);
            if(user == null){
                throw new IllegalArgumentException("Usuario no encontrado con el email proporcionado");
            }
            VerificationCode verificationCode = verificationCodeRepository.findByFkUserAccount(user);
            return verificationCode!=null&&verificationCode.getExpiration().isAfter(LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            System.out.println("Funcion validateResetCode finalizado");
        }
    }

}
