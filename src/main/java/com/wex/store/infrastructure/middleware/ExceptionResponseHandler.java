package com.wex.store.infrastructure.middleware;

import com.wex.store.application.exception.BusinessException;
import com.wex.store.application.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionResponseHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(ExceptionResponseHandler.class);

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Object> businessException(BusinessException ex, WebRequest request) {
        LOGGER.info("{}. {}", UNPROCESSABLE_ENTITY.getReasonPhrase(), String.join("\n", ex.getErrors()));
        return ResponseEntity
                .status(UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse(UNPROCESSABLE_ENTITY, ex.getErrors()));
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Object> notFoundException(NotFoundException ex, WebRequest request) {
        LOGGER.info("{}. {}", NOT_FOUND.getReasonPhrase(), String.join("\n", ex.getErrors()));
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new ErrorResponse(NOT_FOUND, ex.getErrors()));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception ex, WebRequest request) {
        LOGGER.error("{}. {}", INTERNAL_SERVER_ERROR.getReasonPhrase(), ex.getMessage());
        // ex.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse(INTERNAL_SERVER_ERROR, List.of("Something went wrong! Please try again later.")));
    }

    static class ErrorResponse {
        private final int code;
        private final String reason;
        private final List<String> errors;
        private final String timestamp;

        public ErrorResponse(HttpStatus status, List<String> errors) {
            this.code = status.value();
            this.reason = status.getReasonPhrase();
            this.errors = errors;
            this.timestamp = Timestamp.from(Instant.now()).toString();
        }

        public int getCode() {
            return code;
        }

        public String getReason() {
            return reason;
        }

        public List<String> getErrors() {
            return errors;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }
}
