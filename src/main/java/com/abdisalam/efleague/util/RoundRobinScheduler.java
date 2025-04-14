package com.abdisalam.efleague.util;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RoundRobinScheduler {

    public static List<List<Matchup>> generateMatchups(List<Team> teams, Season season) {
        List<List<Matchup>> rounds = new ArrayList<>();

        List<Team> tempTeams = new ArrayList<>(teams);
        if (tempTeams.size() % 2 != 0) tempTeams.add(null); // BYE team

        int numRounds = tempTeams.size() - 1;
        int gamesPerRound = tempTeams.size() / 2;

        for (int round = 0; round < numRounds; round++) {
            List<Matchup> matchupsThisRound = new ArrayList<>();

            for (int i = 0; i < gamesPerRound; i++) {
                Team home = tempTeams.get(i);
                Team away = tempTeams.get(tempTeams.size() - 1 - i);

                if (home != null && away != null) {
                    Matchup matchup = new Matchup();
                    matchup.setHomeTeam(home);
                    matchup.setAwayTeam(away);
                    matchup.setSeason(season);
                    matchup.setRound(round + 1);
                    matchupsThisRound.add(matchup);
                }
            }

            rounds.add(matchupsThisRound);

            // Rotate teams
            Team last = tempTeams.remove(tempTeams.size() - 1);
            tempTeams.add(1, last);
        }

        return rounds;
    }
}
