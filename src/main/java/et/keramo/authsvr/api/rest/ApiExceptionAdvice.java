package et.keramo.authsvr.api.rest;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.google.gson.JsonParseException;
import et.keramo.authsvr.api.rest.ApiErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ApiExceptionAdvice {

    private static final String ERROR_LOG_FORMAT = "HTTP %d Error requesting [%s] %s";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiErrorDto> handleException(HttpServletRequest request, MethodArgumentNotValidException e) {
        this.errorLogging(e, HttpStatus.BAD_REQUEST, request);

        BindingResult result = e.getBindingResult();
        Map<String, Object> errors = new HashMap<>();

        if (result.hasFieldErrors()) {
            Map<String, String> fieldErrors = new HashMap<>();

            for (FieldError err : result.getFieldErrors()) {
                fieldErrors.put(err.getField(), err.getDefaultMessage());
            }
            errors.put("field", fieldErrors);
        }

        if (result.hasGlobalErrors()) {
            List<String> globalErrors = new ArrayList<>();

            for (ObjectError err : result.getGlobalErrors()) {
                globalErrors.add(err.getDefaultMessage());
            }
            errors.put("global", globalErrors);
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ApiErrorDto> handleException(HttpServletRequest request, HttpMessageNotReadableException e) {
        this.errorLogging(e, HttpStatus.BAD_REQUEST, request);

        String mesaage = e.getMessage();

        if (e.getCause() instanceof UnrecognizedPropertyException) {
            mesaage = ((UnrecognizedPropertyException) e.getCause()).getPropertyName() + "은(는) 유효하지 않은 필드입니다.";
        }

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(HttpStatus.BAD_REQUEST, mesaage));
    }

    @ExceptionHandler(HttpStatusCodeException.class)
    protected ResponseEntity<ApiErrorDto> handleException(HttpServletRequest request, HttpStatusCodeException e) {
        this.errorLogging(e, e.getStatusCode(), request);

        return ResponseEntity
                .status(e.getStatusCode())
                .body(new ApiErrorDto(e.getRawStatusCode(), e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ApiErrorDto> handleException(HttpServletRequest request, Exception e) {
        this.errorLogging(e, HttpStatus.BAD_REQUEST, request);

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ApiErrorDto(HttpStatus.BAD_REQUEST, e.getMessage()));
    }


    private void errorLogging(Exception e, HttpStatus httpStatus, HttpServletRequest request) {
        String message = String.format(ERROR_LOG_FORMAT, httpStatus.value(), request.getMethod(), request.getRequestURI());
        log.error(message, e);
    }

}
