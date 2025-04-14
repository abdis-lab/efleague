package com.abdisalam.efleague;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import com.abdisalam.efleague.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class TeamServiceTest {


//    private TeamRepository teamRepository;
//    private UserRepository userRepository;
//    private TeamService teamService;
//
//
//
//    @BeforeEach
//    void setUp(){
//        //Mock the repositories
//        teamRepository = Mockito.mock(TeamRepository.class);
//        userRepository = Mockito.mock(UserRepository.class);
//
//
//        //Create the teamService and inject the mocked repositories
//        teamService = new TeamService(teamRepository, userRepository);
//    }
//
//
//
//
//
//    @Test
//    void testCreateTeamWithValidCaptain(){
//
//        //Create a Captain
//        User captain = new User();
//        captain.setId(2L);
//        captain.setUsername("Captain234");
//        captain.setRole(User.Role.CAPTAIN);
//
//
//        //Create a team
//        Team team = new Team();
//        team.setName("Team A");
//        team.setCaptain(captain);
//
//
//
//        //Mock the findById method to return the captain
//        when(userRepository.findById(2L)).thenReturn(Optional.of(captain));
//
//        //Mock the save method to return the saved team
//        when(teamRepository.save(team)).thenReturn(team);
//
//
//        //Call the saveTeam method to create a team
//        Team savedTeam = teamService.saveTeam(team);
//
//
//
//        //Validate that the saved team has the correct properties
//        assertEquals("Team A", savedTeam.getName());
//        assertEquals("Captain234", savedTeam.getCaptain().getUsername());
//
//
//    }
//
//
//
//    @Test
//    void testCreateTeamWithInvalidCaptain(){
//
//        //Create a player
//        User player = new User();
//        player.setId(1L);
//        player.setUsername("player123");
//        player.setRole(User.Role.PLAYER);
//
//
//        //Create a TEam with a player as a captain (invalid case)
//        Team team = new Team();
//        team.setName("Team A");
//        team.setCaptain(player);
//
//
//
//        //Mock the findById method to return the player
//        when(userRepository.findById(1L)).thenReturn(Optional.of(player));
//
//
//
//        //Expect an IllegalStateException when saving the team
//        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
//            teamService.saveTeam(team);
//        });
//
//
//        //Validate the error message
//        assertEquals("User must be captain to create a team", exception.getMessage());
//
//


//    }




}
