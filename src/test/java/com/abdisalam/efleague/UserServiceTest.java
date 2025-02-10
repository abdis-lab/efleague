package com.abdisalam.efleague;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UserServiceTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;
    @InjectMocks
    private TeamService teamService;

    @BeforeEach  
    void setUp(){
        userService = new UserService(userRepository, passwordEncoder, null , null);
    }


    @Test
    void testSaveUser(){
        //Create A new User (captain) to save
        User captain = new User();
        captain.setUsername("Captain123");
        captain.setPassword("password123");
        captain.setRole(User.Role.CAPTAIN);


        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");


        //Mock the repository save method to return the user when saved
        when(userRepository.save(captain)).thenReturn(captain);


        // Call the saveUser method to save the user
        User savedUser = userService.saveUser(captain);


        // Validate that the saved user has the same properties as the one we saved
        assertEquals("Captain123", savedUser.getUsername());
        assertEquals("password123", savedUser.getPassword());
        assertEquals(User.Role.CAPTAIN, savedUser.getRole());

    }


    @Test
    void testFindUserById(){
        //Create a new user to test the retrieval
        User player = new User();
        player.setId(1L);
        player.setUsername("player123");
        player.setRole(User.Role.PLAYER);


        //Mock the findById method to return the user
        when(userRepository.findById(1L)).thenReturn(Optional.of(player));


        //Call the findUserById method to find the user
        Optional<User> foundUser = userService.findUserById(1L);


        //Validate the user is present and has the correct properties
        assertTrue(foundUser.isPresent());
        assertEquals("player123", foundUser.get().getUsername());
        assertEquals(User.Role.PLAYER, foundUser.get().getRole());

    }




    @Test
    void testAssignCaptainToTeam(){
        // Create a captain and a team
        User captain = new User();
        captain.setId(1L);
        captain.setUsername("captain123");
        captain.setRole(User.Role.CAPTAIN);


        Team team = new Team();
        team.setId(100L);
        team.setName("Team A");


        // Mock the findById methods for user and team
        when(userRepository.findById(1L)).thenReturn(Optional.of(captain));
        when(teamRepository.findById(100L)).thenReturn(Optional.of(team));


        //Assign the captain to the team
        userService.assignPlayerToTeam(1L, 100L);


        //Validate that the captain is correctly set
        assertEquals("captain123", team.getCaptain().getUsername());


        //Validate that the captain is in the players list
        assertEquals(1, team.getUserPlayers().size());
        assertEquals("captain123", team.getUserPlayers().get(0).getUsername());

    }




    @Test
    void testAssignSecondCaptainToTeamShouldThrowException(){
        //create two captains and a team
        User captain1 = new User();
        captain1.setId(1L);
        captain1.setUsername("captain1");
        captain1.setRole(User.Role.CAPTAIN);


        User captain2 = new User();
        captain2.setId(2L);
        captain2.setUsername("captain2");
        captain2.setRole(User.Role.CAPTAIN);



        Team team = new Team();
        team.setId(100L);
        team.setName("Team A");
        team.setCaptain(captain1); // Set captain1 as the initial captain


        //Mock the findById methods for user and team
        when(userRepository.findById(2L)).thenReturn(Optional.of(captain2));
        when(teamRepository.findById(100L)).thenReturn(Optional.of(team));

        //Try to assign a second captain, which should throw an exception
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userService.assignPlayerToTeam(2L, 100L);
        });


        //validate the error message
        assertEquals("Each team can only have one captain.", exception.getMessage());
    }




}
