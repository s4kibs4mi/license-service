package ninja.sakib.licenseservice.controllers;

import ninja.sakib.licenseservice.exceptions.*;
import ninja.sakib.licenseservice.shared.dto.ApiFailureResp;
import ninja.sakib.licenseservice.shared.dto.ApiSuccessResp;
import ninja.sakib.licenseservice.shared.errorcodes.ErrorCode;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.sql.SQLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiFailureResp> handleException(Exception ex) {
        ex.printStackTrace();

        return ResponseEntity
                .internalServerError()
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiFailureResp> handleException(HttpMessageNotReadableException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiFailureResp> handleException(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiFailureResp> handleException(ResourceNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(UserCredentialsIncorrectException.class)
    public ResponseEntity<ApiFailureResp> handleException(UserCredentialsIncorrectException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(UserNotAuthenticatedException.class)
    public ResponseEntity<ApiFailureResp> handleException(UserNotAuthenticatedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ApiFailureResp
                        .builder()
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiFailureResp> handleException(MethodArgumentNotValidException ex) {
        Map<String, List<String>> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(
                        Collectors.groupingBy(fieldError -> getJsonFieldName(fieldError.getField()),
                                Collectors.mapping(DefaultMessageSourceResolvable::getDefaultMessage, Collectors.toList())
                        ));

        return ResponseEntity
                .badRequest()
                .body(ApiFailureResp
                        .builder()
                        .errorMessage("Invalid data")
                        .errors(errors)
                        .build()
                );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiFailureResp> handleException(HandlerMethodValidationException ex) {
        Map<String, List<String>> errors = ex.
                getParameterValidationResults()
                .stream()
                .collect(
                        Collectors.groupingBy(
                                parameterValidationResult -> Objects.requireNonNull(parameterValidationResult.getMethodParameter().getParameterName()),
                                Collectors.mapping(parameterValidationResult -> parameterValidationResult.getResolvableErrors().stream().map(MessageSourceResolvable::getDefaultMessage).collect(Collectors.joining()), Collectors.toList())
                        )
                );

        return ResponseEntity
                .badRequest()
                .body(ApiFailureResp
                        .builder()
                        .errorMessage("Invalid data")
                        .errors(errors)
                        .build()
                );
    }

    @ExceptionHandler({SQLException.class, DataAccessException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ApiFailureResp> handleSqlException(Exception ex) {
        if (ex.getCause() instanceof ConstraintViolationException sqlException) {
            Map<String, List<String>> errors = new HashMap<>();

            var errorMessage = parseUniqueConstraintViolationException(sqlException.getMessage());
            errorMessage.ifPresent(s -> errors.put(s.getFirst(), List.of(s.getSecond())));

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(ApiFailureResp
                            .builder()
                            .errors(errors)
                            .build()
                    );
        }

        return handleException(new Exception(ex.getMessage()));
    }

    @ExceptionHandler(UserNotRegisteredException.class)
    public ResponseEntity<ApiFailureResp> handleException(UserNotRegisteredException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiFailureResp
                        .builder()
                        .errorCode(ErrorCode.USER_NOT_FOUND)
                        .errorMessage(ex.getMessage())
                        .build()
                );
    }

    private Optional<Pair<String, String>> parseUniqueConstraintViolationException(String message) {
        Pattern pattern = Pattern.compile("Key \\((.*?)\\)=\\((.*?)\\)");
        Matcher matcher = pattern.matcher(message);
        if (matcher.find()) {
            var key = matcher.group(1);
            var value = matcher.group(2) + " already exists";
            return Optional.of(Pair.of(key, value));
        }

        return Optional.empty();
    }

    private String getJsonFieldName(String fieldName) {
        return fieldName.
                chars().
                mapToObj(ch ->
                        Character.isUpperCase(ch) ? "_" + Character.toLowerCase((char) ch) : String.valueOf((char) ch)
                ).
                collect(Collectors.joining());
    }
}
