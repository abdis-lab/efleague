package com.abdisalam.efleague.controller;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import com.abdisalam.efleague.services.MatchUpService;
import com.abdisalam.efleague.services.TeamService;
import com.abdisalam.efleague.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/teams")
public class TeamsController {

    private final TeamService teamService;
    private final UserService userService;
    private final MatchUpService matchUpService;

    public TeamsController(MatchUpService matchUpService,TeamService teamService, UserService userService) {
        this.teamService = teamService;
        this.userService = userService;
        this.matchUpService = matchUpService;
    }

    // ─── 1) ADMIN: list all teams ───────────────────────────────────────────────────
    @GetMapping             // GET /teams
    @PreAuthorize("hasRole('ADMIN')")
    public String listAllTeams(Model model) {
        model.addAttribute("teams", teamService.getAllTeams());
        model.addAttribute("pendingTeams", teamService.getPendingTeams());
        model.addAttribute("captains", userService.getAllCaptain());
        model.addAttribute("team", new Team()); // for your create form
        return "teams";      // src/main/resources/templates/teams.html
    }

    // ─── 2) ADMIN: show create form ───────────────────────────────────────────────
    @GetMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public String showCreateTeamForm(Model model) {
        model.addAttribute("team", new Team());
        model.addAttribute("captains", userService.getAllCaptain());
        return "teams";
    }

    // ─── 3) ADMIN: handle create (AJAX) ───────────────────────────────────────────
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseBody
    public Map<String, Object> createTeam(@ModelAttribute Team team,
                                          @RequestParam Long captainId) {
        Map<String, Object> result = new HashMap<>();
        try {
            User captain = userService.findUserById(captainId)
                    .orElseThrow(() -> new IllegalStateException("Captain not found"));
            team.setCaptain(captain);
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

    // ─── 4) ADMIN: edit form ───────────────────────────────────────────────────────
    @GetMapping("/{teamId}/edit")
    @PreAuthorize("hasRole('ADMIN')")
    public String showEditTeamForm(@PathVariable Long teamId, Model model) {
        Team team = teamService.findTeamById(teamId)
                .orElseThrow(() -> new IllegalStateException("Team not found"));
        if (team.getStatus() == Team.Status.REJECTED) {
            model.addAttribute("error", "Rejected teams cannot be edited.");
            return "error";
        }
        model.addAttribute("team", team);
        model.addAttribute("captains", userService.getAllCaptain());
        model.addAttribute("players", userService.getUnassignedPlayers());
        return "team-edit";
    }

    // ─── 5) ADMIN: assign/remove players ──────────────────────────────────────────
    @PostMapping("/{teamId}/assign-player")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignPlayer(@PathVariable Long teamId,
                               @RequestParam Long playerId) {
        teamService.assignPlayerToTeam(teamId, playerId);
        return "redirect:/teams/" + teamId + "/edit";
    }

    @PostMapping("/{teamId}/remove-player")
    @PreAuthorize("hasRole('ADMIN')")
    public String removePlayer(@PathVariable Long teamId,
                               @RequestParam Long playerId) {
        teamService.removePlayerFromTeam(teamId, playerId);
        return "redirect:/teams/" + teamId + "/edit";
    }

    // ─── 6) ADMIN: approve/reject/update/delete ──────────────────────────────────
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
    public String updateTeam(@PathVariable Long teamId,
                             @RequestParam String name, Model model) {
        try {
            teamService.updateTeam(teamId, name);
            return "redirect:/teams";
        } catch (IllegalStateException e) {
            model.addAttribute("error", e.getMessage());
            return "team-edit";
        }
    }

    @PostMapping("/{teamId}/assign-captain")
    @PreAuthorize("hasRole('ADMIN')")
    public String assignCaptain(@PathVariable Long teamId,
                                @RequestParam Long captainId,
                                RedirectAttributes attrs) {
        try {
            teamService.assignCaptainToTeam(teamId, captainId);
            attrs.addFlashAttribute("message", "Captain assigned successfully.");
        } catch (IllegalStateException e) {
            attrs.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/teams/" + teamId + "/edit";
    }

    @PostMapping("/{teamId}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteTeam(@PathVariable Long teamId) {
        teamService.deleteTeamById(teamId);
        return "redirect:/teams";
    }

    // ─── 7) ANYONE: view a single team’s details ─────────────────────────────────
    @GetMapping("/{id}")
    public String showTeam(@PathVariable Long id, Model model) {
        Team team = teamService.findTeamById(id)
                .orElseThrow(() -> new IllegalStateException("Team not found"));
        model.addAttribute("team", team);
        return "myTeam";   // you can reuse myTeam.html if you like, or make a new one
    }

    // ─── 8) ANYONE: “My Team” for the logged-in user ───────────────────────────────
    @GetMapping("/myTeam")
    public String showMyTeam(Principal principal, Model model) {
        User me = userService.findByUsername(principal.getName())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        Team team = me.getTeam();

        Matchup next = (team != null)
                ? matchUpService.getNextMatchupFor(team)
                : null;

        model.addAttribute("team", me.getTeam());
        model.addAttribute("nextMatchup", next);
        return "myTeam";         // src/main/resources/templates/myTeam.html
    }
}



