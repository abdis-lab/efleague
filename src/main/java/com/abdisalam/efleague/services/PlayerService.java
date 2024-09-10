package com.abdisalam.efleague.services;

import com.abdisalam.efleague.modal.Player;
import com.abdisalam.efleague.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository){
        this.playerRepository = playerRepository;
    }


    public Player savePlayer(Player player){
        return playerRepository.save(player);
    }


    public Optional<Player> getPlayerById(Long id){
        return playerRepository.findById(id);
    }


    public List<Player> getAllPlayers(){
        return playerRepository.findAll();
    }

    public void deletePlayerById(Long id){
        playerRepository.deleteById(id);
    }

}
