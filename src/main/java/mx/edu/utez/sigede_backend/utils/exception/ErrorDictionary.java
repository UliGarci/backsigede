package mx.edu.utez.sigede_backend.utils.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class ErrorDictionary {
    private static final Map<String, String> errorMessages = new HashMap<>();

    static {
        errorMessages.put("user.not.found", "El usuario ingresado no existe.");
        errorMessages.put("user.password.incorrect", "Contraseña incorrecta.");
        errorMessages.put("verification.code.expired", "El código de verificación ha expirado.");
        errorMessages.put("user.password.same_as_old", "La nueva contraseña no debe ser silimar a las anterior.");
    }

    public String getErrorMessage(String errorCode) {
        return errorMessages.getOrDefault(errorCode, "Error desconocido.");
    }
}
