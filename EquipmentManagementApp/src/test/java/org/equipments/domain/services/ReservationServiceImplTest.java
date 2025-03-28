package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.equipments.classes.Reservation;
import org.equipments.classes.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReservationServiceImplTest {
    private ReservationService reservationService;

    @Autowired
    public void ReservationRestService(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    public ReservationServiceImplTest(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @Test
    public void testCreateReservation() {
        ReservationService service = new ReservationServiceImpl(new ReservationRepositoryImpl());
        Reservation reservation = Reservation.builder()
                .reservationId(1)
                .equipment(new Equipment(1, "Excavator", "Heavy Machinery", "Available", "Site A", 10000.0))
                .user(new User(1, "John Doe", "johndoe@example.com"))
                .reservationDate(new Date())
                .returnDate(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000)) // 3 zile mai târziu
                .build();

        service.createReservation(reservation);
        List<Reservation> reservations = service.getAllReservations();
        assertEquals(1, reservations.size());
        assertEquals("Excavator", reservations.get(0).getEquipment().getName());
    }

    @Test
    public void testCancelReservation() {
        ReservationService service = new ReservationServiceImpl(new ReservationRepositoryImpl());
        Reservation reservation = Reservation.builder()
                .reservationId(1)
                .equipment(new Equipment(1, "Excavator", "Heavy Machinery", "Available", "Site A", 10000.0))
                .user(new User(1, "John Doe", "johndoe@example.com"))
                .reservationDate(new Date())
                .returnDate(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000))
                .build();

        service.createReservation(reservation);
        service.cancelReservation(1);
        List<Reservation> reservations = service.getAllReservations();
        assertEquals(0, reservations.size());
    }

    @Test
    public void testCalculateReservationDuration() {
        ReservationService service = new ReservationServiceImpl(new ReservationRepositoryImpl());
        Reservation reservation = Reservation.builder()
                .reservationId(1)
                .equipment(new Equipment(1, "Excavator", "Heavy Machinery", "Available", "Site A", 10000.0))
                .user(new User(1, "John Doe", "johndoe@example.com"))
                .reservationDate(new Date())
                .returnDate(new Date(System.currentTimeMillis() + 5 * 24 * 60 * 60 * 1000)) // 5 zile mai tarziu
                .build();

        service.createReservation(reservation);
        long duration = service.calculateReservationDuration(1);
        assertEquals(5, duration);
    }};