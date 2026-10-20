package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.domainEntities.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResp {

    private Long userId;
    private String department;
    private String name;
    private String email;
    private boolean active;
    private User.UserRole role;

}
