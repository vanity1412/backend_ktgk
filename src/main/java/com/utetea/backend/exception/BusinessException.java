package com.utetea.backend.exception;
//VU VAN THONG 23162098
import lombok.Getter;
import org.springframework.http.HttpStatus;
//VU VAN THONG 23162098
@Getter
public class BusinessException extends RuntimeException {
    private final HttpStatus status;
    
    public BusinessException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
    }
    
    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
