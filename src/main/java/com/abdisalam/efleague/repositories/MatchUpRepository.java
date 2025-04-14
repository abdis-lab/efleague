package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Season;
import com.abdisalam.efleague.modal.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchUpRepository extends JpaRepository<Matchup, Long> {


    boolean existsByHomeTeamAndAwayTeamAndGameDateTime(
            Team homeTeam,
            Team awayTeam,
            LocalDateTime gameDateTime
    );


    List<Matchup> findBySeason(Season season);


    List<Matchup> findBySeasonOrderByGameDateTimeAsc(Season season);

    @Query("SELECT DISTINCT m.round FROM Matchup m ORDER BY m.round")
    List<Integer> findDistinctRounds();


    List<Matchup> findByRound(int round);
}
