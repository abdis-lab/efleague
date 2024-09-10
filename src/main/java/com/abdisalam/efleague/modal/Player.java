package com.abdisalam.efleague.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "First name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50")
    private String lastName;

    @NotNull(message = "Age cannot be null")
    @Size(min = 18, max = 99, message = "Age must be 18 and Up")
    private int age;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

}
