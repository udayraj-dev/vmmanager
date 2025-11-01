package me.udayraj.vmmanager.dto;

import java.time.LocalDateTime;

/**
 * A standard structure for API error responses.
 *
 * @param timestamp The time the error occurred.
 * @param status    The HTTP status code.
 * @param error     The HTTP status message.
 * @param message   A developer-friendly description of the error.
 * @param path      The request path that caused the error.
 */
public record ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {}