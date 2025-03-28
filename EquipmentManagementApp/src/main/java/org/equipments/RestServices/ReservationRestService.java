package org.equipments.RestServices;

import org.equipments.classes.Reservation;
import org.equipments.domain.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("http://localhost:8080/api/reservations")
public class ReservationRestService {

    @Autowired
    private ReservationService reservationService;

    // Create a new reservation
    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestBody Reservation reservation) {
        reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Cancel a reservation by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable("id") int reservationId) {
        reservationService.cancelReservation(reservationId);
        return ResponseEntity.noContent().build();
    }

    // Get details of a specific reservation
    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationDetails(@PathVariable("id") int reservationId) {
        try {
            Reservation reservation = reservationService.getReservationDetails(reservationId);
            return ResponseEntity.ok(reservation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Get all reservations
    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationService.getAllReservations();
    }

    // Calculate the duration of a specific reservation
    @GetMapping("/{id}/duration")
    public ResponseEntity<Long> calculateReservationDuration(@PathVariable("id") int reservationId) {
        try {
            long duration = reservationService.calculateReservationDuration(reservationId);
            return ResponseEntity.ok(duration);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
