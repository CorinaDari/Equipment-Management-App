package org.equipments.classes;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Component
@Entity

@Table(name = "users")
public class User {
    @Id @GeneratedValue
    private int userId;
    private String name;
    private String role;
    private String department;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;


    public User(int i, String johnDoe, String mail) {

    }
}
