package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Player;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.PlayerRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder, PlayerRepository playerRepository, TeamRepository teamRepository){
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Override method to load users by username for authentication
    @Transactional
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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
            if(user.getRole() == User.Role.ROLE_CAPTAIN) {
                //Ensure the captain isn't already assigned to another team
                Optional<Team> existingTeam = teamRepository.findByCaptain(user);
                if (existingTeam.isPresent()) {
                    throw new IllegalStateException("User is already a captain of another team.");
                }
                team.setCaptain(user);

            }

            // Add the user to the team's players list (both captains and players go here)
                user.setTeam(team); // Associate user with the team
                team.getUserPlayers().add(user); // Add user to the team's players list
                teamRepository.save(team); // Save the team

        }else {
            throw new IllegalStateException("User or Team not Found");
        }


    }

    public Optional<User> findByUsername(String username){
        return userRepository.findByUsername(username);
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
