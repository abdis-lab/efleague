package com.abdisalam.efleague.modal;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "season_id")
    private Season season;


    @Enumerated(EnumType.STRING)
    private GameResult result;

    private int round;


    public enum GameResult{
        HOME_TEAM_WIN, AWAY_TEAM_WIN
    }

    @PostLoad
    private void calculateResult(){
        if(homeTeamScore == null || awayTeamScore == null) return;
        this.result = (homeTeamScore > awayTeamScore) ? GameResult.HOME_TEAM_WIN : GameResult.AWAY_TEAM_WIN;
    }

}
