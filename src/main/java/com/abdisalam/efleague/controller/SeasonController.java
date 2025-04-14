package com.abdisalam.efleague.controller;


import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.repositories.SeasonRepository;
import com.abdisalam.efleague.services.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/seasons")
@Validated
public class SeasonController {


    private final SeasonRepository seasonRepository;
    private final SeasonService seasonService;

    @Autowired
    public SeasonController(SeasonRepository seasonRepository, SeasonService seasonService) {
        this.seasonRepository = seasonRepository;
        this.seasonService = seasonService;
    }

    // Show all seasons
    @GetMapping
    public String showSeasons(Model model) {
        model.addAttribute("seasons", seasonRepository.findAll());
        return "list";  // you'll create this HTML
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("season", new Season());
        return "createSeason";
    }


    @PostMapping("/create")
    public String createSeason(@ModelAttribute Season season) {
        seasonService.saveSeason(season);
        return "redirect:/seasons";
    }



    // Activate a specific season
    @PostMapping("/activate/{id}")
    public String activateSeason(@PathVariable Long id) {
        seasonService.activateSeason(id);
        return "redirect:/seasons";
    }





}
