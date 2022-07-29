package et.keramo.authsvr.exception;

import et.keramo.authsvr.service.auth.AuthErrorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.ConnectException;

@NoArgsConstructor
@Getter
public class AuthServerException extends Exception {

    public static final String INVALID_REQUEST = "invalid_request";
    public static final String CONNECT_REFUSED = "connect_refused";

    private Integer statusCode;
    public String errorCode;

    public AuthServerException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public AuthServerException(String errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    @Override
    public String toString() {
        return new AuthErrorDto(this).toString();
    }

    public static AuthServerException create(Throwable t) {
        Throwable cause = t.getCause();

        if (cause instanceof ConnectException) {
            return new AuthServerException(CONNECT_REFUSED, "Connect Refused", cause);
        }
        return new AuthServerException(INVALID_REQUEST, "Auth Server Error", cause);
    }

}
