package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamService teamService;
    private final UserService userService;

    public TeamsController(TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
    }

    @GetMapping
    public String getAllTeams(Model model) {
        List<Team> teams = teamService.getAllTeams();
        List<Team> pendingTeams = teamService.getPendingTeams();
        List<User> captains = userService.getAllCaptain();


        model.addAttribute("teams", teams);
        model.addAttribute("team", new Team());
        model.addAttribute("pendingTeams", pendingTeams);
        model.addAttribute("captains", captains);
        return "teams";
    }

    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("captains", userService.getAllCaptain());
        return "teams";
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> createTeam(@ModelAttribute("team") Team team, @RequestParam Long captainId) {
        Map<String, Object> result = new HashMap<>();

        try {
            Optional<User> captainOpt = userService.findUserById(captainId);
            if (captainOpt.isEmpty()) {
                throw new IllegalStateException("Captain not found!");
            }

            team.setCaptain(captainOpt.get());
            team.setStatus(Team.Status.PENDING);
            teamService.saveTeam(team);

            result.put("success", true);
            result.put("message", "Team created Successfully!");
        } catch (IllegalStateException e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }

    @GetMapping("/{teamId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditTeamForm(@PathVariable Long teamId, Model model) {
        Optional<Team> teamOpt = teamService.findTeamById(teamId);
        if (teamOpt.isPresent()) {
            Team team = teamOpt.get();

            // Prevent editing rejected teams
            if (team.getStatus() == Team.Status.REJECTED) {
                model.addAttribute("error", "Rejected teams cannot be edited.");
                return "error";
            }

            List<User> availablePlayers = userService.getUnassignedPlayers();
            List<User> availableCaptains = userService.getAllCaptain();
            System.out.println("Available Players: " + availablePlayers);




            model.addAttribute("team", team);
            model.addAttribute("captains", availableCaptains);
            model.addAttribute("players", availablePlayers);
            return "team-edit";
        } else {
            model.addAttribute("error", "Team not found");
            return "error";
        }
    }

    @PostMapping("/{teamId}/assign-player")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignPlayer(@PathVariable Long teamId, @RequestParam Long playerId, Model model) {
        try {
            teamService.assignPlayerToTeam(teamId, playerId);
            return "redirect:/teams/" + teamId + "/edit";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "team-edit";
        }
    }

    @PostMapping("/{teamId}/removePlayer")
    @PreAuthorize("hasRole('ADMIN')")
    public String removePlayerFromTeam(@PathVariable Long teamId, @RequestParam Long playerId, Model model) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return "redirect:/teams/" + teamId + "/edit";
    }



    @PostMapping("/{teamId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public String approveTeam(@PathVariable Long teamId) {
        teamService.approveTeam(teamId);
        return "redirect:/teams";
    }

    @PostMapping("/{teamId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public String rejectTeam(@PathVariable Long teamId) {
        teamService.rejectTeam(teamId);
        return "redirect:/teams";
    }


    @PostMapping("/{teamId}/update")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateTeam(@PathVariable Long teamId, @RequestParam String name, Model model){
        try{
            teamService.updateTeam(teamId, name);
            return "redirect:/teams";
        }catch (IllegalStateException e){
            model.addAttribute("error", e.getMessage());
            return "team-edit";
        }
    }





    @PostMapping("/{teamId}/assign-captain")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignCaptain(@PathVariable Long teamId, @RequestParam Long captainId, RedirectAttributes redirectAttributes){
        try{
            teamService.assignCaptainToTeam(teamId, captainId);
            redirectAttributes.addFlashAttribute("message", "Captain assigned successfully.");
        }catch (IllegalStateException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
       return "redirect:/teams/edit";

    }








    @PostMapping("/{teamId}/delete")
    @DeleteMapping("/{teamId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTeam(@PathVariable Long teamId){
        teamService.deleteTeamById(teamId);
        return "redirect:/teams";
    }







}



