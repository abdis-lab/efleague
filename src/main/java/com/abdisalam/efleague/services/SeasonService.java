package com.abdisalam.efleague.services;


import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.repositories.MatchUpRepository;
import com.abdisalam.efleague.repositories.SeasonRepository;
import com.abdisalam.efleague.repositories.TeamRepository;
import org.springframework.stereotype.Service;
import com.abdisalam.efleague.util.RoundRobinScheduler;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SeasonService {

    private final SeasonRepository seasonRepository;
    private final MatchUpRepository matchUpRepository;
    private final TeamRepository teamRepository;


    public SeasonService(TeamRepository teamRepository,SeasonRepository seasonRepository, MatchUpRepository matchUpRepository){
        this.seasonRepository = seasonRepository;
        this.matchUpRepository = matchUpRepository;
        this.teamRepository = teamRepository;
    }



    public Season saveSeason(Season season){
        return seasonRepository.save(season);
    }


    public Optional<Season> findSeasonById(Long id){
        return seasonRepository.findById(id);
    }


    public void activateSeason(Long seasonId) {
        // 1. Deactivate all seasons
        seasonRepository.findAll().forEach(season -> {
            season.setActive(false);
            seasonRepository.save(season);
        });

        // 2. Activate the selected season
        Season seasonToActivate = seasonRepository.findById(seasonId)
                .orElseThrow(() -> new IllegalArgumentException("Season not found"));
        seasonToActivate.setActive(true);
        seasonRepository.save(seasonToActivate);

        // 3. Get all APPROVED teams
        List<Team> approvedTeams = teamRepository.findApprovedTeams();

        // 4. Generate matchups using RoundRobinScheduler util
        List<List<Matchup>> rounds = RoundRobinScheduler.generateMatchups(approvedTeams, seasonToActivate);
        LocalDateTime startDate = seasonToActivate.getStartDate().atStartOfDay();

        for (int i = 0; i < rounds.size(); i++) {
            LocalDateTime roundDate = startDate.plusWeeks(i);
            List<Matchup> matchups = rounds.get(i);

            for (Matchup m : matchups) {
                m.setGameDateTime(roundDate);
                m.setSeason(seasonToActivate);
            }

            matchUpRepository.saveAll(matchups);
        }

        // Done! You should now see weekly matchups in the DB, per season
    }

    public Season getActiveSeason() {
        return seasonRepository.findByActiveTrue().orElse(null);
    }

    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }


}
