package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class PlayoffService {


    private final TeamRepository teamRepository;

    public PlayoffService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }


    public List<Team> getPlayoffTeams(){
        //Fetch all the teams and sort them by their performance
        List<Team> allTeams = teamRepository.findAll();
        allTeams.sort(Comparator.comparingInt(Team::getWins).reversed());


        return allTeams.subList(0, Math.min(4, allTeams.size()));
    }


}
