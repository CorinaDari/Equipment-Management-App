package rest.services;

import org.equipments.classes.Reservation;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(OrderAnnotation.class)
public class Test_ReservationRestService {

    private static final Logger logger = Logger.getLogger(Test_ReservationRestService.class.getName());
    private static final String serviceURL = "http://localhost:8080/api/reservations";
    private final RestTemplate restTemplate = new RestTemplate();

    @Test
    @Order(1)
    public void test1_GetAllReservations() {
        logger.info("DEBUG: Testing GET all reservations...");
        HttpHeaders headers = generateHeaders();

        List<Reservation> reservations = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Reservation>>() {}
        ).getBody();

        logger.info("Reservations fetched: " + reservations);
    }

    @Test
    @Order(2)
    public void test2_AddReservation() {
        logger.info("DEBUG: Testing POST create reservation...");
        HttpHeaders headers = generateHeaders();

        Reservation reservation = new Reservation();
        reservation.setId(1);
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now().plusHours(2));

        String response = restTemplate.exchange(
                serviceURL,
                HttpMethod.POST,
                new HttpEntity<>(reservation, headers),
                String.class
        ).getBody();

        logger.info("Created reservation: " + response);
    }

    @Test
    @Order(3)
    public void test3_GetReservationDetails() {
        logger.info("DEBUG: Testing GET reservation details...");
        HttpHeaders headers = generateHeaders();

        int reservationId = 1;

        Reservation reservation = restTemplate.exchange(
                serviceURL + "/" + reservationId,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Reservation.class
        ).getBody();

        logger.info("Reservation details: " + reservation);
    }

    @Test
    @Order(4)
    public void test4_CancelReservation() {
        logger.info("DEBUG: Testing DELETE cancel reservation...");
        HttpHeaders headers = generateHeaders();

        int reservationId = 1;

        restTemplate.exchange(
                serviceURL + "/" + reservationId,
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class
        );

        logger.info("Cancelled reservation with ID: " + reservationId);

        // Verify deletion
        List<Reservation> reservations = restTemplate.exchange(
                serviceURL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                new ParameterizedTypeReference<List<Reservation>>() {}
        ).getBody();

        boolean reservationExists = reservations.stream().anyMatch(r -> r.getId() == reservationId);
        assertTrue(!reservationExists, "The reservation was not deleted!");
    }

    @Test
    @Order(5)
    public void test5_CalculateReservationDuration() {
        logger.info("DEBUG: Testing GET reservation duration...");
        HttpHeaders headers = generateHeaders();

        int reservationId = 1;

        Long duration = restTemplate.exchange(
                serviceURL + "/" + reservationId + "/duration",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Long.class
        ).getBody();

        logger.info("Reservation duration: " + duration + " hours");
    }

    private HttpHeaders generateHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
