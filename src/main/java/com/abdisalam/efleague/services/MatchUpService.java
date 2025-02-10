package com.abdisalam.efleague.services;
import java.util.*;
import java.util.logging.Logger;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
public class MatchUpService {

    private final MatchUpRepository matchUpRepository;
    private final TeamRepository teamRepository;

    private static final Logger logger = Logger.getLogger(MatchUpService.class.getName());

    public MatchUpService(MatchUpRepository matchUpRepository, TeamRepository teamRepository){
        this.matchUpRepository = matchUpRepository;
        this.teamRepository = teamRepository;
    }



    //Schedule a new matchup
    @Transactional
    public Matchup saveMatchup(Matchup matchup){
        return matchUpRepository.save(matchup);
    }


    //find a match up by id
    @Transactional
    public Optional<Matchup> findMatchupById(Long id){
        return matchUpRepository.findById(id);
    }

    //get all matchups
    @Transactional
    public List<Matchup> getAllMatchups(){
        return matchUpRepository.findAll();
    }


    // Update the score for a match up
    @Transactional
    public Matchup updateMatchupScore(Long id, Integer homeTeamScore, Integer awayTeamScore){
        Matchup matchup = matchUpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Matchup not found!"));

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
    }


    //update the win/loss count for the teams
    private void updateTeamResults(Team winner, Team loser){
        winner.incrementWins();
        loser.incrementLosses();
        teamRepository.save(winner);
        teamRepository.save(loser);
    }


    //Generate a round Robin schedule for the league
    @Transactional
    public List<Matchup> generateRoundRobinSchedule(LocalDateTime startDate, boolean doubleRoundRobin, Season season){
        //List of teams object
        List<Team> teams = teamRepository.findAll();

        List<Matchup> matchups = new ArrayList<>();

        // Adjust start date to the next Saturday if it's not already a saturday
        LocalDateTime nextSaturday = getNextSaturday(startDate);


        //Limit each team to 8 games
        int gamesPerTeam = 8;

        //Make sure there is at least 2 teams and it dosnt exceed 12
        if(teams.size() > 12){
            throw new IllegalStateException("Maximum 12 teams allowed.");
        }

        //Generate matchups with a limit of 8 games per team
 //       int[] gamesPlayed = new int[numTeams];
        Map<Team, Integer> gamesPlayed = new HashMap<>();
        teams.forEach(team -> gamesPlayed.put(team, 0));

        logger.info("Starting Round Robin Scheduling...");

        //Scheduling each team against every other team
        for(int i = 0; i < teams.size() - 1; i++){
            for(int j = i + 1; j < teams.size(); j++){
                Team homeTeam = teams.get(i);
                Team awayTeam = teams.get(j);

                if(gamesPlayed.get(homeTeam) < gamesPerTeam && gamesPlayed.get(awayTeam) < gamesPerTeam){


                    logger.info("Scheduling matchup between " + homeTeam.getName() + " and " + awayTeam.getName());

                    boolean matchupExists = matchUpRepository.existsByHomeTeamAndAwayTeamAndGameDateTime(
                            homeTeam,
                            awayTeam,
                            nextSaturday
                    );

                    if(!matchupExists(homeTeam, awayTeam, nextSaturday)){
                        //schedule the matchup
                        matchups.add(createMatchup(homeTeam, awayTeam, nextSaturday, season));
                        gamesPlayed.put(homeTeam, gamesPlayed.get(homeTeam) + 1);
                        gamesPlayed.put(awayTeam, gamesPlayed.get(awayTeam) + 1);
                        nextSaturday = nextSaturday.plus(1, ChronoUnit.WEEKS);
                    }


                    //double round-robin matchups (away vs home)
                    if (doubleRoundRobin && !matchupExists(awayTeam, homeTeam, nextSaturday)) {
                        matchups.add(createMatchup(awayTeam, homeTeam, nextSaturday, season));
                        gamesPlayed.put(awayTeam, gamesPlayed.get(awayTeam) + 1);
                        gamesPlayed.put(homeTeam, gamesPlayed.get(homeTeam) + 1);
                        nextSaturday = nextSaturday.plus(1, ChronoUnit.WEEKS);
                    }
                }
            }
        }

        logger.info("Total matchups created: " + matchups.size());
        return matchUpRepository.saveAll(matchups);
    }

    private boolean matchupExists(Team homeTeam, Team awayTeam, LocalDateTime date){
        return matchUpRepository.existsByHomeTeamAndAwayTeamAndGameDateTime(homeTeam, awayTeam, date);
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
