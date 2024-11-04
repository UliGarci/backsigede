package mx.edu.utez.sigede_backend.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomResponse<T> {
    private int status;
    private String message;
    private boolean error;
    private T data;
}
