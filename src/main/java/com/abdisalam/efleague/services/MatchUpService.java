package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MatchUpService {

    private final MatchUpRepository matchUpRepository;
    private final TeamRepository teamRepository;

    public MatchUpService(MatchUpRepository matchUpRepository, TeamRepository teamRepository){
        this.matchUpRepository = matchUpRepository;
        this.teamRepository = teamRepository;
    }



    //Schedule a new matchup
    public Matchup saveMatchup(Matchup matchup){
        return matchUpRepository.save(matchup);
    }


    //find a match up by id
    public Optional<Matchup> findMatchupById(Long id){
        return matchUpRepository.findById(id);
    }



    //get all matchups
    public List<Matchup> getAllMatchups(){
        return matchUpRepository.findAll();
    }


    // Update the score for a match up
    public Matchup updateMatchupScore(Long id, Integer homeTeamScore, Integer awayTeamScore){
        Optional<Matchup> existingMatchup = matchUpRepository.findById(id);


        if (existingMatchup.isPresent()){
            Matchup matchup = existingMatchup.get();

            matchup.setHomeTeamScore(homeTeamScore);
            matchup.setAwayTeamScore(awayTeamScore);

            //Determine the winner and upadte wins/losses for teams
            if(homeTeamScore > awayTeamScore){
                matchup.setResult(Matchup.GameResult.HOME_TEAM_WIN);
                updateTeamResults(matchup.getHomeTeam(), matchup.getAwayTeam());
            }else if(awayTeamScore > homeTeamScore){
                matchup.setResult(Matchup.GameResult.AWAY_TEAM_WIN);
                updateTeamResults(matchup.getAwayTeam(), matchup.getHomeTeam());
            }

            return matchUpRepository.save(matchup);

        }else {
            throw new RuntimeException("Matchup Not Found");
        }

    }


    //update the win/loss count for the teams
    private void updateTeamResults(Team winner, Team loser){
        winner.incrementWins();
        loser.incrementLosses();
        teamRepository.save(winner);
        teamRepository.save(loser);
    }





    //Generate a round Robin schedule for the league
    public List<Matchup> generateRoundRobinSchedule(LocalDateTime startDate, boolean doubleRoundRobin, Season season){
        //List of teams object
        List<Team> teams = teamRepository.findAll();

        List<Matchup> matchups = new ArrayList<>();

        // Adjust start date to the next Saturday if it's not already a saturday
        LocalDateTime nextSaturday = getNextSaturday(startDate);

        int numTeams = teams.size();
        int gamesPerTeam = 8;


        //Make sure there is at least 2 teams
        if(numTeams > 12){
            throw new IllegalStateException("Maximum number of teams allowed is 12.");
        }


        //Generate matchups with a limit of 8 games per team
        int[] gamesPlayed = new int[numTeams];


        //Scheduling each team against every other team
        for(int i = 0; i < numTeams - 1; i++){
            for(int j = i + 1; j < numTeams; j++){
                if(gamesPlayed[i] < gamesPerTeam && gamesPlayed[j] < gamesPerTeam){
                    Team homeTeam = teams.get(i);
                    Team awayTeam = teams.get(j);


                    //Schedule first round matchups (home vs away)
                    matchups.add(createMatchup(homeTeam, awayTeam, nextSaturday, season));
                    nextSaturday = nextSaturday.plus(1, ChronoUnit.WEEKS);


                    gamesPlayed[i]++;
                    gamesPlayed[j]++;

                    if(gamesPlayed[i] == gamesPerTeam || gamesPlayed[j] == gamesPerTeam){
                        break;
                    }


                    //double round-robin matchups (away vs home)
                    if(doubleRoundRobin){
                        matchups.add(createMatchup(awayTeam, homeTeam, nextSaturday, season ));
                        nextSaturday = nextSaturday.plus(1, ChronoUnit.WEEKS);
                    }
                }
            }
        }

        return matchUpRepository.saveAll(matchups);
    }

    private LocalDateTime getNextSaturday(LocalDateTime date){
        //If the date is already Saturday, return it otherwise adjust to the next saturday
        if(date.getDayOfWeek() == DayOfWeek.SATURDAY){
            return date;
        }

        long daysToAdd = DayOfWeek.SATURDAY.getValue() - date.getDayOfWeek().getValue();

        //Calculating the days to add to get to the next Saturday
        if(daysToAdd < 0){
            daysToAdd += 7;
        }

        return date.plusDays(daysToAdd);
    }




    //Helper method to create a matchup object
    private Matchup createMatchup(Team homeTeam, Team awayTeam, LocalDateTime gameDateTime, Season season){

        Matchup matchup  = new Matchup();

        matchup.setHomeTeam(homeTeam);
        matchup.setAwayTeam(awayTeam);
        matchup.setGameDateTime(gameDateTime);
        matchup.setSeason(season);
        return matchup;
    }





}
