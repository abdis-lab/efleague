package com.abdisalam.efleague.services;
import java.util.*;
import java.util.logging.Logger;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.SeasonRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import com.abdisalam.efleague.util.RoundRobinScheduler;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
public class MatchUpService {

    private final MatchUpRepository matchUpRepository;
    private final TeamRepository teamRepository;
    private final SeasonRepository seasonRepository;
    private final TeamService teamService;
    private final SeasonService seasonService;

    private static final Logger logger = Logger.getLogger(MatchUpService.class.getName());

    public MatchUpService(SeasonService seasonService,TeamService teamService,SeasonRepository seasonRepository,MatchUpRepository matchUpRepository, TeamRepository teamRepository){
        this.matchUpRepository = matchUpRepository;
        this.teamRepository = teamRepository;
        this.seasonRepository = seasonRepository;
        this.teamService = teamService;
        this.seasonService = seasonService;
    }


    @Transactional
    public void generateMatchupsForActiveSeason() {
        Season activeSeason = seasonRepository.findByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active season found"));

        List<Team> approvedTeams = teamRepository.findApprovedTeams();
        if (approvedTeams.size() < 2) {
            throw new IllegalStateException("Need at least 2 approved teams.");
        }

        // Generate rounds
        List<List<Matchup>> rounds = RoundRobinScheduler.generateMatchups(approvedTeams, activeSeason);

        // Flatten them
        List<Matchup> allMatchups = rounds.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        // Suppose we schedule them weekly, starting from "today" or from a Season start date:
        LocalDateTime baseDate = activeSeason.getStartDate().atStartOfDay();
        if (baseDate == null) {
            // fallback if you don't have a startDate
            baseDate = LocalDateTime.now();
        }

        // For each matchup, set a date based on the 'round'
        for (Matchup m : allMatchups) {
            int roundIndex = m.getRound() - 1; // if round is 1-based
            m.setGameDateTime(baseDate.plusWeeks(roundIndex));
        }

        // Finally, save
        matchUpRepository.saveAll(allMatchups);
    }



    public Map<Integer, List<Matchup>> getMatchupsGroupedByRoundForActiveSeason() {
        Season activeSeason = seasonRepository.findByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active season found"));

        List<Matchup> allMatchups = matchUpRepository.findBySeasonOrderByGameDateTimeAsc(activeSeason);

        // ...and group by round, producing a Map<Integer, List<Matchup>>
        return allMatchups.stream()
                .collect(Collectors.groupingBy(
                        Matchup::getRound,               // group by round property
                        TreeMap::new,                    // use a TreeMap so keys (rounds) come in sorted order
                        Collectors.toList()              // produce a list of matchups for each round
                ));
    }


    public List<Matchup> getUpcomingMatchups() {
        Season activeSeason = seasonRepository.findByActiveTrue()
                .orElseThrow(() -> new IllegalStateException("No active season"));

        return matchUpRepository.findBySeasonOrderByGameDateTimeAsc(activeSeason)
                .stream()
                .filter(m -> m.getHomeTeam() != null && m.getAwayTeam() != null)
                .toList();
    }


    public List<Integer> getAllRounds(){
        return matchUpRepository.findDistinctRounds();
    }


    public List<Matchup> getMatchupsByRound(int round){
        return matchUpRepository.findByRound(round);
    }

    public Matchup getNextMatchupFor(Team team){
        return matchUpRepository
                .findFirstByHomeTeamOrAwayTeamAndGameDateTimeAfterOrderByGameDateTimeAsc(
                        team, team, LocalDateTime.now())
                .orElse(null);
    }

}
