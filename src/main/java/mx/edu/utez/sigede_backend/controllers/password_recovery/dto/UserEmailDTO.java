package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserEmailDTO {
    @NotBlank(message = "user.email.notnull")
    private String userEmail;
}
