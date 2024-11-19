package com.abdisalam.efleague.controller;


import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final TeamService teamService;
    private final PasswordEncoder passwordEncoder;



    public UserController(TeamService teamService, UserService userService, PasswordEncoder passwordEncoder){
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
    }


    @GetMapping("/signup")
    public String getAllUsers(Model model){
        model.addAttribute("user", new User());
        return "signUp";
    }


    @PostMapping("/signup")
    public String registerUser(
            @ModelAttribute("user") User user,
            @RequestParam String role,
            @RequestParam(required = false) String teamPreference,
            Model model
    ){
        //Encode the password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.valueOf(role));

        //Set the team preference
        if(teamPreference != null && !teamPreference.trim().isEmpty()){
            user.setTeamPreference(teamPreference);
        }


        try{
            userService.saveUser(user);
            return "redirect:/users/login";
        }catch (Exception e){
            model.addAttribute("error", "An error Accured while saving user");
        }

        return "signUp";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }




    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id){
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/assign-to-team")
    public ResponseEntity<String> assignPlayerToTeam(@RequestParam Long playerId,@RequestParam Long teamId){
        userService.assignPlayerToTeam(playerId, teamId);
        return new ResponseEntity<>("Player assigned to team", HttpStatus.OK);
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user,
            Model model
    ){
        userService.saveUser(user);
        model.addAttribute("message", "User registered Successfully");
        return "schedule-games";
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User userDetails){
        Optional<User> existingUser = userService.findUserById(id);


        if(existingUser.isPresent()){
            User user = existingUser.get();

            user.setUsername(userDetails.getUsername());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());

            User updateUser = userService.saveUser(user);

            return ResponseEntity.ok(updateUser);
        }else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }


}
