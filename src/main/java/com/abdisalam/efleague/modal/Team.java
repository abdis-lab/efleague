package com.abdisalam.efleague.modal;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "team_id")
    private List<User> userPlayers = new ArrayList<>();


    public void addPlayer(User userPlayer){
        if(this.userPlayers.size() < 10){
            this.userPlayers.add(userPlayer);
        }else{
            throw new IllegalStateException("Team cannot have more than 10 players");
        }
    }


    private int wins = 0;
    private int losses = 0;


    public void incrementWins(){
        this.wins++;
    }

    public void incrementLosses(){
        this.losses++;
    }


}
