package et.keramo.authsvr.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class FreeIPAException extends Exception {

    public static final String NOT_FOUND = "4001";

    public String errorCode;
    public Integer statusCode;

    public FreeIPAException(String message, Throwable cause, String errorCode, Integer statusCode) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }
}
