package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    private static final int MAX_TEAMS = 12;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository){
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }


    //Save a New Team
    public Team saveTeam(Team team){
        List<Team> currentTeams = teamRepository.findAll();

        if(currentTeams.size() >= MAX_TEAMS){
            throw new IllegalStateException("Maximum number of teams (12)");
        }

        Optional<User> captainOpt = userRepository.findById(team.getCaptain().getId());
        if(captainOpt.isPresent() && captainOpt.get().getRole() == User.Role.CAPTAIN){
            team.setCaptain(captainOpt.get());
            return teamRepository.save(team);
        }else {
            throw new IllegalStateException("User must be captain to create a team");
        }
    }


    public Team updateTeam(Long teamId, String newName){
        Optional<Team> teamOptional = teamRepository.findById(teamId);

        if(teamOptional.isPresent()){
            Team team = teamOptional.get();
            team.setName(newName);// Update team name
            return teamRepository.save(team);// Save the updated team
        }else {
            throw new IllegalStateException("Team Not Found");
        }
    }

    public Team removePlayerFromTeam(Long teamId, Long playerId){
        Optional<Team> teamOptional = teamRepository.findById(teamId);
        Optional<User> userOptional = userRepository.findById(playerId);



        if(teamOptional.isPresent() && userOptional.isPresent()){

            Team team = teamOptional.get();
            User user = userOptional.get();

            //Make sure the player is on the team before removing
            if(team.getUserPlayers().contains(user)){
                user.setTeam(null);
                team.getUserPlayers().remove(user);
                teamRepository.save(team);
                userRepository.save(user);
                return teamRepository.save(team);
            }else {
                throw new IllegalStateException("Player is not on the team.");
            }

        }else {
            throw new IllegalStateException("Team or Player not Found.");
        }
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
