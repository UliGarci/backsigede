package mx.edu.utez.sigede_backend.controllers.password_recovery.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ValidateCodeDTO {
    private String code;
    private UUID userId;
}
