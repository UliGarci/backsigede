package mx.edu.utez.sigede_backend.security;

public class AuthenticationProcessingException extends RuntimeException{
    public AuthenticationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}