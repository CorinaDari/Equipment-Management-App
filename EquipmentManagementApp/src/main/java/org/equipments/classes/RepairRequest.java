package org.equipments.classes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class RepairRequest {
    @Id @GeneratedValue
    private int requestId;
    @ManyToOne
    @JoinColumn(name = "equipment_id", referencedColumnName = "equipmentId")
    private Equipment equipment;

    private String issueDescription;
    private Date requestDate;
    private String status;
    @ManyToOne
    @JoinColumn(name = "technician_id", referencedColumnName = "userId")
    private User technician;

    @ElementCollection
    @CollectionTable(
            name = "repair_activities", // Numele tabelei intermediare
            joinColumns = @JoinColumn(name = "repair_request_id") // Cheia străină către tabela RepairRequest
    )
    @Column(name = "activity") // Numele coloanei pentru elementele colecției
    private List<String> repairActivities;

}
