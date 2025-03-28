package org.equipments.classes;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class Maintenance {
    @Id @GeneratedValue
    private int maintenanceId;
    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipmentId")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name = "technician_id", referencedColumnName = "userId")
    private User technician;

    private Date maintenanceDate;
    private String maintenanceType;
    private String description;
}
