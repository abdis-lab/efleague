package com.abdisalam.efleague.controller;


import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService){
        this.userService = userService;
    }


    @GetMapping("/register")
    public String getAllUsers(Model model){
        model.addAttribute("user", new User());
        return "user-create";
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
