package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class PasswordChangeResponseDTO {
    private String newPassword;
    private UUID userId;

    public PasswordChangeResponseDTO(String password, UUID userAccountId) {
    }
}
