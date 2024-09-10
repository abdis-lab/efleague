package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository){
        this.teamRepository = teamRepository;
    }


    //Save a New Team
    public Team saveTeam(Team team){
        return teamRepository.save(team);
    }


    //Find a team by ID
    public Optional<Team> findTeamById(Long id){
        return teamRepository.findById(id);
    }


    //Get All Teams
    public List<Team> getAllTeams(){
        return teamRepository.findAll();
    }


    //Delete a Team by ID
    public void deleteTeamById(Long id){
        teamRepository.deleteById(id);
    }






}
