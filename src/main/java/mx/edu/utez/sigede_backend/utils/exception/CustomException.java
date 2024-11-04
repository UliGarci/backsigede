package mx.edu.utez.sigede_backend.utils.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException{
    private final String errorCode;

    public CustomException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
