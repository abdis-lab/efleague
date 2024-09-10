package com.abdisalam.efleague.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @NotNull(message = "Team name cannot be null")
    @Size(min = 2, max = 50, message = "Team name must be between 2 and 50.")
    private String name;

    @ManyToOne
    @JoinColumn(name = "captain_id")
    private User captain;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Player> players;


}
