package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Matchup;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.abdisalam.efleague.modal.Matchup;
import lombok.extern.slf4j.Slf4j;
import com.abdisalam.efleague.services.MatchUpService;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.validation.Valid;
import org.apache.juli.logging.Log;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.util.List;


@Slf4j
@Controller
public class LandingPage {

    private final MatchUpService matchUpService;

    public LandingPage(MatchUpService matchUpService){
        this.matchUpService = matchUpService;
    }


    @GetMapping("/landingPage")
    public String showLandingPage(
            Model model,
            @RequestParam(name = "round", required = false) Integer round){


        log.info("User selected round = {}", round);

        // If round is null or empty, show "all" or "upcoming" matchups.
        // If you prefer "none," then you'd leave it empty.
        List<Matchup> matchups = (round == null)
                ? matchUpService.getUpcomingMatchups()  // or getAllMatchups(), whichever you want
                : matchUpService.getMatchupsByRound(round);

        log.info("Found {} matchups for round {}", matchups.size(), round);

        List<Integer> allRounds = matchUpService.getAllRounds();

        // Add to model
        model.addAttribute("matchups", matchups);
        model.addAttribute("allRounds", allRounds);
        // Store the 'selectedRound' so we can highlight it in the <option> menu
        model.addAttribute("selectedRound", round);

        return "landingPage";
    }


}
