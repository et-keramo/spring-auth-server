package et.keramo.authsvr.api.rest.auth;

import et.keramo.authsvr.exception.AuthServerException;
import lombok.Data;

@Data
public class OAuth2ErrorDto {

    private String error;
    private String error_description;

    public OAuth2ErrorDto(AuthServerException e) {
        this.error = e.getErrorCode();
        this.error_description = e.getMessage();
    }

    @Override
    public String toString() {
        return this.getSummary();
    }

    private String getSummary() {
        StringBuilder builder = new StringBuilder();

        String delim = "";

        String error = this.getError();
        if (error != null) {
            builder.append(delim).append("error=\"").append(error).append("\"");
            delim = ", ";
        }

        String errorMessage = this.getError_description();
        if (errorMessage != null) {
            builder.append(delim).append("error_description=\"").append(errorMessage).append("\"");
        }

        return builder.toString();
    }

}
