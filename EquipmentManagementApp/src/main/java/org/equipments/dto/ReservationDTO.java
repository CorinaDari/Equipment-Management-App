package org.equipments.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {

    private int reservationId;
    private int equipmentId;
    private int userId;
    private Date reservationDate;
    private Date returnDate;

}
