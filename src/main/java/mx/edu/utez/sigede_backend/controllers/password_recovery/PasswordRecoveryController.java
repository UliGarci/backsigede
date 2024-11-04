package mx.edu.utez.sigede_backend.controllers.password_recovery;

import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeRequestDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.PasswordChangeResponseDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.UserEmailDTO;
import mx.edu.utez.sigede_backend.controllers.password_recovery.dto.ValidateCodeDTO;
import mx.edu.utez.sigede_backend.services.password_recovery.PasswordRecoveryService;
import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/credentials/api/recovery-password")
@CrossOrigin(origins = {"*"})
public class PasswordRecoveryController {
    private final PasswordRecoveryService passwordRecoveryService;

    public PasswordRecoveryController(PasswordRecoveryService passwordRecoveryService) {
        this.passwordRecoveryService = passwordRecoveryService;
    }

    @PostMapping("/send-verification-code")
    public ResponseEntity<CustomResponse<UUID>> sendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            UUID userId = passwordRecoveryService.sendVerificationCode(userEmailDTO.getUserEmail());
            CustomResponse<UUID> response = new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId
            );
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            CustomResponse<UUID> errorResponse = new CustomResponse<>(
                    404, "El usuario que ha ingresado no existe.", true, null);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }

    @PostMapping("/resend-verification-code")
    public ResponseEntity<CustomResponse<UUID>> resendVerificationCode(@RequestBody UserEmailDTO userEmailDTO) {
        try {
            UUID userId = passwordRecoveryService.resendVerificationCode(userEmailDTO.getUserEmail());
            CustomResponse<UUID> response = new CustomResponse<>(
                    200, "Código de verificación enviado correctamente.", false, userId);
            return ResponseEntity.ok(response);
        } catch (CustomException e) {
            CustomResponse<UUID> errorResponse = new CustomResponse<>(
                    400, "Ocurrio un error al enviar el código de verificación.", true, null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PostMapping("/validate-verification-code")
    public ResponseEntity<CustomResponse<UUID>> validateVerificationCode(@RequestBody ValidateCodeDTO validateCodeDTO) {
        try {
            boolean result = passwordRecoveryService.validateVerificationCode(validateCodeDTO.getCode(), validateCodeDTO.getUserId());
            if (result) {
                passwordRecoveryService.deleteVerificationCode(validateCodeDTO.getUserId());
                CustomResponse<UUID> response = new CustomResponse<>(
                        200, "El código es valido.", false, validateCodeDTO.getUserId()
                );
                return ResponseEntity.ok(response);
            } else {
                CustomResponse<UUID> response = new CustomResponse<>(400, "El código ingresado no es valido.", true, null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } catch (CustomException e) {
            CustomResponse<UUID> errorResponse = new CustomResponse<>(
                    400, e.getMessage(), true, null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<CustomResponse<PasswordChangeResponseDTO>> changePassword(@RequestBody PasswordChangeRequestDTO passwordChangeRequestDTO) {
        try {
            PasswordChangeResponseDTO responseDTO = passwordRecoveryService.changePassword(
                    passwordChangeRequestDTO.getNewPassword(), passwordChangeRequestDTO.getUserId());
            CustomResponse<PasswordChangeResponseDTO> response = new CustomResponse<>(
                    200, "La contraseña ha sido cambiada correctamente", false,
                    responseDTO
            );
            return ResponseEntity.ok(response);
            //servicio para enviar email
        } catch (CustomException e) {
            CustomResponse<PasswordChangeResponseDTO> errorResponse = new CustomResponse<>(
                    400, e.getMessage(), true, null
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
}
