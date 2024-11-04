package mx.edu.utez.sigede_backend.controllers.mailcontroller;

import jakarta.mail.MessagingException;
import mx.edu.utez.sigede_backend.services.mailservice.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/mail")
@CrossOrigin(origins = "*")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/password-reset")
    public ResponseEntity<String> sendPasswordResetCode(@RequestParam String email){
        try {
            mailService.sendPasswordResetCode(email);
            return ResponseEntity.ok("Código de recuperación enviado al correo");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error al enviar el correo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        } finally {
            System.out.println("Controlador sendPasswordResetCode finalizado");
        }
    }

    @PostMapping("/validate-reset-code")
    public ResponseEntity<String> validateResetCode(@RequestParam String email){
        boolean isValid = mailService.validateResetCode(email);
        return isValid ?ResponseEntity.ok("Código valido"):ResponseEntity.status(400).body("Código invalido o expirado");
    }

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerNewAdmin(@RequestParam String email){
        try {
            mailService.sendTemporaryPassword(email);
            return ResponseEntity.ok("Administrador registrado. Se envió el acceso al correo");
        } catch (MessagingException e) {
            return ResponseEntity.status(500).body("Error al enviar el correo");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        } finally {
            System.out.println("Controlador registerNewAdmin finalizado");
        }
    }
}
