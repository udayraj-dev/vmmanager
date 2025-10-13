package me.udayraj.vmmanager.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import me.udayraj.vmmanager.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import me.udayraj.vmmanager.service.MessageService;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    private final MessageService messageService;

    public GlobalExceptionHandler(MessageService messageService) {
        this.messageService = messageService;
    }
    
    @ExceptionHandler(VmConnectionException.class)
    public ResponseEntity<ErrorResponse> handleVmConnectionException(VmConnectionException ex, WebRequest request) {
        logger.error("Connection error processing request [{}]: {}", getRequestDescription(request), ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(VmPersistenceException.class)
    public ResponseEntity<ErrorResponse> handleVmPersistenceException(VmPersistenceException ex, WebRequest request) {
        logger.error("Persistence error processing request [{}]: {}", getRequestDescription(request), ex.getMessage(), ex);
        return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        logger.warn("Bad request processing request [{}]: {}", getRequestDescription(request), ex.getMessage());
        return buildErrorResponse(ex, HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        String validationErrorMessage = "Validation failed for the following fields: " + errors;
        logger.warn("Validation error processing request [{}]: {}", getRequestDescription(request), validationErrorMessage);

        // We can pass the map of errors in the response if we adjust the ErrorResponse DTO,
        // but for now, a detailed string message is very effective.
        return buildErrorResponse(ex, validationErrorMessage, HttpStatus.BAD_REQUEST, request);
    }

    // A catch-all for any other unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, WebRequest request) {
        logger.error("An unexpected error occurred processing request [{}]: {}", getRequestDescription(request), ex.getMessage(), ex);
        String message = messageService.getMessage("error.unexpected");
        return buildErrorResponse(ex, message, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, WebRequest request) {
        return buildErrorResponse(ex, ex.getMessage(), status, request);
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, String message, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                getRequestUri(request));
        return new ResponseEntity<>(errorResponse, status);
    }

    private String getRequestUri(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            return servletWebRequest.getRequest().getRequestURI();
        }
        return "N/A";
    }

    /**
     * Safely extracts the HTTP method and URI from the WebRequest for logging.
     *
     * @param request The incoming web request.
     * @return A string in the format "METHOD /uri".
     */
    private String getRequestDescription(WebRequest request) {
        if (request instanceof ServletWebRequest servletWebRequest) {
            HttpServletRequest httpServletRequest = servletWebRequest.getRequest();
            return httpServletRequest.getMethod() + " " + httpServletRequest.getRequestURI();
        }
        return request.getDescription(false); // Fallback for non-servlet requests
    }
}