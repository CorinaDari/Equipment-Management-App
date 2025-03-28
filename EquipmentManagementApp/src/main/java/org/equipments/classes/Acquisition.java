package org.equipments.classes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Acquisition {
    @Id @GeneratedValue
    private int acquisitionId;
    private Date acquisitionDate;
    private double totalAmount;

    @OneToMany(mappedBy = "acquisition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Equipment> equipmentList= new ArrayList<>();
}
