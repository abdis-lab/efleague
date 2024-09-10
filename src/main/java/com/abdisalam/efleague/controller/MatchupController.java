package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.services.MatchUpService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/matchups")
@Validated
public class MatchupController {


    private final MatchUpService matchUpService;

    public MatchupController(MatchUpService matchUpService){
        this.matchUpService = matchUpService;
    }


    @GetMapping
    public List<Matchup> getAllMatchups(){
        return matchUpService.getAllMatchups();
    }


    @GetMapping("/{id}")
    public ResponseEntity<Matchup> getMatchupById(@PathVariable Long id){
        Optional<Matchup> matchup = matchUpService.findMatchupById(id);
        return matchup.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }



    @PostMapping
    public ResponseEntity<Matchup> createMatchup(@Valid @RequestBody Matchup matchup){
        Matchup savedMatchup = matchUpService.saveMatchup(matchup);
        return new ResponseEntity<>(savedMatchup, HttpStatus.CREATED);
    }


    @PutMapping("/{id}/score")
    public ResponseEntity<Matchup> updateMatchUpScore(
            @PathVariable Long id,
            @RequestParam Integer homeTeamScore,
            @RequestParam Integer awayTeamScore){
        Matchup updatedMatchup = matchUpService.updateMatchupScore(id, homeTeamScore, awayTeamScore);
        return ResponseEntity.ok(updatedMatchup);
    }





    @PostMapping("/round-robin")
    public ResponseEntity<List<Matchup>> generateRoundRobinSchedule(@RequestParam("startDate") String startDate){

        LocalDateTime parsedDate = LocalDateTime.parse(startDate);
        List<Matchup> matchups = matchUpService.generateRoundRobinSchedule(parsedDate);
        return new ResponseEntity<>(matchups, HttpStatus.CREATED);


    }




}
