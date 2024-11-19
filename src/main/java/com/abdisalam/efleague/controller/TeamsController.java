package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/teams")
public class TeamsController {


    private final TeamService teamService;
    private final UserService userService;

    public TeamsController(TeamService teamService, UserService userService){
        this.teamService = teamService; this.userService = userService;
    }


    @GetMapping
    public String getAllTeams(Model model){
        model.addAttribute("teams", teamService.getAllTeams());
        return "teams";
    }

    @GetMapping("/create")
    public String showCreateTeamForm(Model model){
        model.addAttribute("team", new Team());
        model.addAttribute("users", userService.getAllUsers());
        return "team-create";
    }


    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id){
        Optional<Team> team = teamService.findTeamById(id);
        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{teamId}/edit")
    public String showEditTeamForm(@PathVariable Long teamId, Model model){
        Optional<Team> teamOpt = teamService.findTeamById(teamId);
        if(teamOpt.isPresent()){
            model.addAttribute("team", teamOpt.get());
            model.addAttribute("users", userService.getAllUsers());
            return "team-edit";//Return the 'team-edit.html template
        }else {
            model.addAttribute("error", "Team not found");
            return "error";
        }
    }


    //Update the team form
    @PostMapping("/{teamId}/update")
    public String updateTeam(@PathVariable Long teamId,
                             @PathVariable String name,
                             Model model){

        teamService.updateTeam(teamId, name);
        return "redirect:/teams/{teamId}/edit";
    }


    //Add a player to a team
    @PostMapping("/{teamId}/addPlayer")
    public String addPlayerToTeam(@PathVariable Long teamId,
                                  @RequestParam Long playerId,
                                  Model model){
        userService.assignPlayerToTeam(playerId, teamId);
        return "redirect:/teams/{teamId}/edit";
    }


    @PostMapping("/{teamId}/removePlayer")
    public String removePlayerFromTeam(@PathVariable Long teamId,
                                       @RequestParam Long playerId,
                                       Model model){
        teamService.removePlayerFromTeam(teamId, playerId);
        return "redirect:/teams/{teamId}/edit";
    }




//    @PostMapping
//    public ResponseEntity<Team> createTeam(@Valid @RequestBody Team team){
//        Team savedTeam = teamService.saveTeam(team);
//        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
//    }
//

    @PostMapping("/create")
    public String createTeam(@ModelAttribute("team") Team team, Model model){
        try{
            teamService.saveTeam(team);
            return "redirect:/teams";
        }catch(IllegalStateException e){
            model.addAttribute("error", e.getMessage());
            return "team-create";
        }
    }


    @PostMapping("/assign-user")
    public String assignUserToTeam(@ModelAttribute("userId") Long userId, @ModelAttribute("teamId") Long teamId, Model model){
        try{
            userService.assignPlayerToTeam(userId, teamId); // Assign the user (captain or player )
            model.addAttribute("message", "User assigned to the team successfully!");

        }catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
        }
        return "landingPage";
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
