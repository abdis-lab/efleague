package com.abdisalam.efleague.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Matchup {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "home_team_id")
    @NotNull(message = "Home team cannot be null")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "away_team_id")
    @NotNull(message = "Away team cannot be null")
    private Team awayTeam;


    @NotNull(message = "Game date and time cannot be null")
    private LocalDateTime gameDateTime;

    private Integer homeTeamScore;
    private Integer awayTeamScore;


    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;


    @Enumerated(EnumType.STRING)
    private GameResult result;


    public enum GameResult{
        HOME_TEAM_WIN, AWAY_TEAM_WIN
    }


}
