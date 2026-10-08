package org.example.domainEntities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipment_category")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class EquipmentCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "equipment_category_id")
    private Long equipmentCategoryId;

    @Column(nullable = false, unique = true)
    private String name;

    private String description;
}