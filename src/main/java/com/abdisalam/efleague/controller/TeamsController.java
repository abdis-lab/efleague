package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.services.TeamService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/teams")
public class TeamsController {


    private final TeamService teamService;

    public TeamsController(TeamService teamService){
        this.teamService = teamService;
    }


//    @GetMapping
//    public List<Team> getAllTeams(){
//        return teamService.getAllTeams();
//    }

    @GetMapping("/create")
    public String showCreateTeamForm(Model model){
        model.addAttribute("team", new Team());
        return "team-create";
    }


    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id){
        Optional<Team> team = teamService.findTeamById(id);
        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


//    @PostMapping
//    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team){
//        Team savedTeam = teamService.saveTeam(team);
//        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
//    }
//

    @PostMapping("/create")
    public String createTeam(@ModelAttribute("team") Team team, Model model){
        teamService.saveTeam(team);
        model.addAttribute("message", "Team created successfully");
        return "team-create";
    }


    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id,@Valid @RequestBody Team teamDetails){
        Optional<Team> existingTeam = teamService.findTeamById(id);


        if(existingTeam.isPresent()){
            Team team = existingTeam.get();
            team.setName(teamDetails.getName());
            Team updatedTeam = teamService.saveTeam(team);
            return ResponseEntity.ok(updatedTeam);
        }else {
            return ResponseEntity.notFound().build();
        }
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id){
        teamService.deleteTeamById(id);
        return ResponseEntity.noContent().build();
    }

}
