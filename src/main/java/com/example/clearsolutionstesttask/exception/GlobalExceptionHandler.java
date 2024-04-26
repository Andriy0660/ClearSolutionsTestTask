package com.example.clearsolutionstesttask.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return ExceptionBuilder.buildExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleNotFoundException(NotFoundException ex) {
        return ExceptionBuilder.buildExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SQLException.class)
    public ResponseEntity<?> handleSqlException(SQLException ex) {
        return ExceptionBuilder.buildExceptionResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException ex) {
        return ExceptionBuilder.buildExceptionResponse(
                new BadRequestException("Invalid request parameters"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingParameter(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String errorMessage = "Required parameter is missing: " + parameterName;
        return ExceptionBuilder.buildExceptionResponse(
                new BadRequestException(errorMessage), HttpStatus.BAD_REQUEST);
    }
}


