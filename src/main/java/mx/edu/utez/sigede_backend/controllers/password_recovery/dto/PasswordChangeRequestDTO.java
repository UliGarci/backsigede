package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PasswordChangeRequestDTO {
    private String newPassword;
    private UUID userId;
}
