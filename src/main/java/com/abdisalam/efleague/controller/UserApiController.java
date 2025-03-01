package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;
    private final TeamService teamService;
    private final PasswordEncoder passwordEncoder;

    public UserApiController(UserService userService, TeamService teamService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.teamService = teamService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register a new user (REST)
    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        // Validate input
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }

        // Encode password and assign default role
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(User.Role.ROLE_USER); // Default role

        User savedUser = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> user = userService.findUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user (with password encoding)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User userDetails) {
        Optional<User> existingUser = userService.findUserById(id);

        if (existingUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User user = existingUser.get();
        user.setUsername(userDetails.getUsername());
        user.setPassword(passwordEncoder.encode(userDetails.getPassword())); // Encode new password
        user.setRole(userDetails.getRole());

        User updatedUser = userService.saveUser(user);
        return ResponseEntity.ok(updatedUser);
    }
//
//    // Assign player to team
//    @PostMapping("/{playerId}/assign-to-team/{teamId}")
//    public ResponseEntity<String> assignPlayerToTeam(
//            @PathVariable Long playerId,
//            @PathVariable Long teamId
//    ) {
//        userService.assignPlayerToTeam(playerId, teamId);
//        return ResponseEntity.ok("Player assigned to team");
//    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    // Get all users (example)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
}