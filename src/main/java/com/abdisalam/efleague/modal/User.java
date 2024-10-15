package com.abdisalam.efleague.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.management.relation.Role;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Username cannot be Empty")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50")
    private String username;

    @NotNull(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters Long")
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    public enum Role{
        ADMIN,
        CAPTAIN,
        PLAYER
    }

}
