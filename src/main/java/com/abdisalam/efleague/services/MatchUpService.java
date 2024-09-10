package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

            return matchUpRepository.save(matchup);

        }else {
            throw new RuntimeException("Matchup Not Found");
        }

    }



    //Generate a round Robin schedule for the league
    public List<Matchup> generateRoundRobinSchedule(LocalDateTime startDate){
        //List of teams object
        List<Team> teams = teamRepository.findAll();

        List<Matchup> matchups = new ArrayList<>();

        int numTeams = teams.size();

        //Make sure there is at least 2 teams
        if(numTeams < 2){
            throw new IllegalStateException("There must be at least 2 teams");
        }


        //Scheduling each team against every other team
        for(int i = 0; i < numTeams - 1; i++){
            for(int j = i + 1; j < numTeams; j++){
                Team homeTeam = teams.get(i);
                Team awayTeam = teams.get(j);

                //Schedule first round matchups (home vs away)
                matchups.add(createMatchup(homeTeam, awayTeam, startDate.plusDays(matchups.size())));

                //double round robin matchups (away vs home)
                matchups.add(createMatchup(awayTeam, homeTeam, startDate.plusDays(matchups.size())));
            }
        }

        return matchUpRepository.saveAll(matchups);


    }




    //Helper method to create a matchup object
    private Matchup createMatchup(Team homeTeam, Team awayTeam, LocalDateTime gameDateTime){

        Matchup matchup  = new Matchup();

        matchup.setHomeTeam(homeTeam);
        matchup.setAwayTeam(awayTeam);
        matchup.setGameDateTime(gameDateTime);
        return matchup;


    }





}
