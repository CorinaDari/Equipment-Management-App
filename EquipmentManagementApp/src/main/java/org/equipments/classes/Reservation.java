package org.equipments.classes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class Reservation {
    @Id @GeneratedValue
    private int reservationId;
    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipmentId")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private Date reservationDate;
    private Date returnDate;

    public void setId(int i) {
        this.reservationId = reservationId;
    }

    public void setStartTime(LocalDateTime now) {
        this.reservationDate = reservationDate;
    }

    public void setEndTime(LocalDateTime localDateTime) {
    }

    public int getId() {
        return reservationId;
    }
}
