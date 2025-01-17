package mx.edu.utez.sigede_backend.advice;

import mx.edu.utez.sigede_backend.utils.CustomResponse;
import mx.edu.utez.sigede_backend.utils.exception.CustomException;
import mx.edu.utez.sigede_backend.utils.exception.ErrorDictionary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestControllerAdvice
public class ErrorHandleClass extends ResponseEntityExceptionHandler {
    private final ErrorDictionary errorDictionary;

    public ErrorHandleClass(ErrorDictionary errorDictionary) {
        this.errorDictionary = errorDictionary;
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach((error)  -> {
            String customMessage = errorDictionary.getErrorMessage(error.getDefaultMessage());
            errors.put(error.getField(), customMessage);
        });

        return new ResponseEntity<>(new CustomResponse<>(400, "Error en los datos enviados", true, errors), headers, status);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        String errorCode = ex.getMessage();
        String errorMessage = errorDictionary.getErrorMessage(errorCode);
        Map<String, String> errors = new HashMap<>();
        errors.put("error", errorMessage);

        return new ResponseEntity<>(new CustomResponse<>(400, "Error en los datos enviados.", true, errors), HttpStatus.BAD_REQUEST);
    }
}
