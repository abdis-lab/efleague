package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Player;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.PlayerRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository,PlayerRepository playerRepository, TeamRepository teamRepository){
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
    }


    //Assign a player to an existing team
    public void assignPlayerToTeam(Long playerId, Long teamId){
        Optional<User> userOpt = userRepository.findById(playerId);
        Optional<Team> teamOpt = teamRepository.findById(teamId);

        if(userOpt.isPresent() && teamOpt.isPresent()){
            User user = userOpt.get();
            Team team = teamOpt.get();

            //Only allow assignment if the user is a PLAYER (not a captain)
            if(user.getRole() == User.Role.PLAYER){
                team.getUserPlayers().add(user);
                teamRepository.save(team);
            }else{
                throw new IllegalStateException("Captains cannot be added");
            }
        }else {
            throw new IllegalStateException("User or Team not Found");
        }


    }


    public Optional<User> findUserById(Long id){
        return userRepository.findById(id);
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


    public void deleteUserById(Long id){
        userRepository.deleteById(id);
    }


}
