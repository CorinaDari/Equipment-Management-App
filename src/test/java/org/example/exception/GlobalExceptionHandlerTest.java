package org.example.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler =
            new GlobalExceptionHandler();

    @Test
    void shouldReturn409WhenOptimisticLockingFails() {

        ObjectOptimisticLockingFailureException exception =
                new ObjectOptimisticLockingFailureException(
                        "RepairTicket", 1L);

        ResponseEntity<Map<String, String>> response =
                handler.handleOptimisticLocking(exception);

        assertEquals(
                HttpStatus.CONFLICT,
                response.getStatusCode()
        );

        assertEquals(
                "This ticket was modified by another user. Please refresh and try again.",
                response.getBody().get("message")
        );
    }

    @Test
    void shouldReturn409WhenDatabaseConstraintIsViolated() {

        org.springframework.dao.DataIntegrityViolationException exception =
                new org.springframework.dao.DataIntegrityViolationException(
                        "Unique constraint violation");

        ResponseEntity<Map<String, String>> response =
                handler.handleIntegrityViolation(exception);

        assertEquals(
                HttpStatus.CONFLICT,
                response.getStatusCode()
        );

        assertEquals(
                "An active repair ticket already exists for this equipment",
                response.getBody().get("message")
        );
    }
}