package org.equipments.domain.services;

import org.equipments.classes.Equipment;
import org.equipments.classes.Reservation;
import org.equipments.classes.User;
import org.equipments.dto.ReservationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Service

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @GetMapping
    public List<ReservationDTO> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();

        // Convertește lista de Reservation în lista de ReservationDTO
        return reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @PostMapping
    public ReservationDTO addReservation(@RequestBody ReservationDTO reservationDTO) {
        // Convertește DTO în entitatea Reservation
        Reservation reservation = convertToEntity(reservationDTO);
        Reservation savedReservation = reservationService.saveReservation(reservation);

        return convertToDTO(savedReservation);
    }

    private ReservationDTO convertToDTO(Reservation reservation) {
        return new ReservationDTO(
                reservation.getReservationId(),
                reservation.getEquipment().getEquipmentId(),
                reservation.getUser().getUserId(),
                reservation.getReservationDate(),
                reservation.getReturnDate()
        );
    }

    private Reservation convertToEntity(ReservationDTO reservationDTO) {
        Equipment equipment = new Equipment();
        equipment.setEquipmentId(reservationDTO.getEquipmentId());

        User user = new User();
        user.setUserId(reservationDTO.getUserId());

        return new Reservation(
                reservationDTO.getReservationId(),
                equipment,
                user,
                reservationDTO.getReservationDate(),
                reservationDTO.getReturnDate()
        );
    }
}
