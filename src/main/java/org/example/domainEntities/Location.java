package org.example.domainEntities;
import jakarta.persistence.*;
import lombok.*;
@Entity
@Table(name = "location")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long locationId;

    @Column(nullable = false, unique = true)
    private String name;

    public enum LocationType { HANGAR, WORKSHOP, FIELD, WAREHOUSE }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LocationType type;

    private String description;
}