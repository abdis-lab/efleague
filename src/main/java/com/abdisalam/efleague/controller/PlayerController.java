package com.abdisalam.efleague.controller;


import com.abdisalam.efleague.modal.Player;
import com.abdisalam.efleague.services.PlayerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService){
        this.playerService = playerService;
    }


    @GetMapping
    public List<Player> getAllPlayers(){
        return playerService.getAllPlayers();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        Optional<Player> player = playerService.getPlayerById(id);
        return player.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Player> createPlayer(@RequestBody Player player){
        Player savedPlayer = playerService.savePlayer(player);
        return new ResponseEntity<>(savedPlayer, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player playerDetails){
        Optional<Player> existingPlayer = playerService.getPlayerById(id);

        if(existingPlayer.isPresent()){
            Player player = existingPlayer.get();
            player.setFirstName(playerDetails.getFirstName());
            player.setLastName(playerDetails.getLastName());
            player.setAge(playerDetails.getAge());

            Player updatedPlayer = playerService.savePlayer(player);

            return ResponseEntity.ok(updatedPlayer);
        }else{
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id){
        playerService.deletePlayerById(id);
        return ResponseEntity.noContent().build();
    }


}
