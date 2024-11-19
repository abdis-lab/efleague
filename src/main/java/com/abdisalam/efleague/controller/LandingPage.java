package com.abdisalam.efleague.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingPage {

    @GetMapping("/landingPage")
    public String showLandingPage(){
        return "landingPage";
    }



}
