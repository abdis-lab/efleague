package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.PlayerRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    private final EmailService emailService;

    private final PasswordEncoder passwordEncoder;

    public UserService(EmailService emailService,UserRepository userRepository,PasswordEncoder passwordEncoder, PlayerRepository playerRepository, TeamRepository teamRepository){
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.playerRepository = playerRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }



    public void registerUser(User user) {
        userRepository.save(user);

        // 🔥 DEBUG: Ensure this is being called
        System.out.println("📧 Attempting to send Commissioner Notification...");

        emailService.sendCommissionerNotification(
                "New User Registered: " + user.getUsername(),
                user.getUsername(),
                user.getRole().name() // Convert Enum to String
        );

        System.out.println("✅ Email function executed successfully!");
    }




    @PostConstruct
    public void createDefaultAdmin(){
        Optional<User> existingAdmin = userRepository.findByRole(User.Role.ROLE_ADMIN);


        if(existingAdmin.isEmpty()){
            User admin = new User();
            admin.setUsername("commissioner");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(User.Role.ROLE_ADMIN);
            admin.setTeam(null);


            userRepository.save(admin);
            System.out.println("Commissioner account created: Username -> commissioner, Password -> admin123");
        }
    }



    // Override method to load users by username for authentication
    @Transactional
    public User saveUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
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

    public List<User> getUnassignedPlayers(){
        return userRepository.findByTeamIsNull();// Fetch player with no assigned team
    }

}
