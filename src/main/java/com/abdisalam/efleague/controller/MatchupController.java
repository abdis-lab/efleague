package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.services.MatchUpService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
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

    @GetMapping("/schedule")
    public String scheduleGames(Model model){
        model.addAttribute("matchup", new Matchup());
        return "schedule-games";
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


    @PostMapping("/schedule")
    public String createSchedule(@ModelAttribute("matchup") Matchup matchup,
                                 @RequestParam("startDate") String startDate,
                                 @RequestParam("doubleRoundRobin") boolean doubleRoundRobin,
                                 @RequestParam("seasonId") Long seasonId,
                                 Model model){

        LocalDateTime parsedDate = LocalDateTime.parse(startDate);
        Season season = new Season();
        season.setId(seasonId);


        matchUpService.generateRoundRobinSchedule(parsedDate, doubleRoundRobin, season);
        model.addAttribute("message", "Games scheduled successfully!");
        return "schedule-games";
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
    public ResponseEntity<List<Matchup>> generateRoundRobinSchedule(
            @RequestParam("startDate") String startDate,
            @RequestParam(value = "doubleRoundRobin",
                    defaultValue = "false") boolean doubleRoundRobin,
            Season season
    ){


        LocalDateTime parsedDate = LocalDateTime.parse(startDate);
        List<Matchup> matchups = matchUpService.generateRoundRobinSchedule(parsedDate, doubleRoundRobin, season);
        return new ResponseEntity<>(matchups, HttpStatus.CREATED);


    }




}
