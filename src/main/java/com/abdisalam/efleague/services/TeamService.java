package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
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
        if(teamRepository.findByName(team.getName()).isPresent()){
            throw new IllegalStateException("A Team with this name already Exists.");
        }

        List<Team> currentTeams = teamRepository.findAll();

        if(currentTeams.size() >= MAX_TEAMS){
            throw new IllegalStateException("Maximum number of teams (12)");
        }

        Optional<User> captainOpt = userRepository.findById(team.getCaptain().getId());
        if(captainOpt.isPresent() && captainOpt.get().getRole() == User.Role.ROLE_CAPTAIN){
            team.setCaptain(captainOpt.get());
            team.setStatus(Team.Status.PENDING);
            return teamRepository.save(team);
        }else {
            throw new IllegalStateException("User must be captain to create a team");
        }
    }


//    public Team approveTeam(Long teamId){
//        Team team = teamRepository.findById(teamId).orElseThrow(() -> new IllegalStateException("Team not found!"));
//
//        team.setStatus(Team.Status.APPROVED);
//        return teamRepository.save(team);
//    }


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
                team.getUserPlayers().remove(user);
                user.setTeam(null);
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


    //Comisionar Team Approval
    public Team approveTeam(Long teamId){
        Optional<Team> teamOptional = teamRepository.findById(teamId);


        if(teamOptional.isPresent()){
            Team team = teamOptional.get();
            if(team.getStatus() == Team.Status.PENDING){
                team.setStatus(Team.Status.APPROVED);
                return teamRepository.save(team);
            }else {
                throw new IllegalStateException("Team is already approved or rejected.");
            }
        }else {
            throw new IllegalStateException("Team not found!");
        }
    }

    //Commisionar Rejection
    public Team rejectTeam(Long teamId){
        Optional<Team> teamOptional = teamRepository.findById(teamId);


        if(teamOptional.isPresent()){
            Team team = teamOptional.get();

            if (team.getStatus() == Team.Status.PENDING) {
                team.setStatus(Team.Status.REJECTED);
                return teamRepository.save(team);

            }else {
                throw new IllegalStateException("Team is already approved or rejected!");

            }
        }else {
            throw new IllegalStateException("Team not found!");
        }
    }


    //Testing teams
    @PostConstruct
    public void initializeTestTeams(){
        if(teamRepository.findAll().isEmpty()){

            User captain1 = userRepository.findById(1L).orElseThrow(() -> new RuntimeException("Captain cannot be found!"));
            User captain2 = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("Captain cannot be found!"));


            Team team1 = new Team();
            team1.setName("The Hoopers");
            team1.setCaptain(captain1);
            team1.setStatus(Team.Status.PENDING);
            teamRepository.save(team1);


            Team team2 = new Team();
            team2.setName("Fast Breakers");
            team2.setCaptain(captain2);
            team2.setStatus(Team.Status.PENDING);
            teamRepository.save(team2);


        }
    }




    public List<Team> getPendingTeams(){
        return teamRepository.findPendingTeams();
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
