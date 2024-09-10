package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public User saveUser(User user){
        return userRepository.save(user);
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
