package org.equipments.classes;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity
public class Equipment {
    @Getter
    @Id
    @GeneratedValue
    private int equipmentId;
    private String name;
    private String type;
    private String status;
    private Date acquisitionDate;
    private double price;
    private String location;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Maintenance> maintenanceHistory;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RepairRequest> repairHistory;

    @ManyToOne
    @JoinColumn(name = "id_acquisition", referencedColumnName = "acquisitionId")
    private Acquisition acquisition;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;

    @ManyToMany
    @JoinTable(
            name = "equipment_report",
            joinColumns = @JoinColumn(name = "equipment_id"),
            inverseJoinColumns = @JoinColumn(name = "report_id")
    )
    private List<Report> reports;


    public Equipment(int equipmentId, String name, String type, String status, String location, double price) {
        this.equipmentId = equipmentId;
        this.name = name;
        this.type = type;
        this.status = status;
        this.location = location;
        this.price = price;
    }

}
