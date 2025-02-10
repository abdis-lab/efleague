package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Matchup;
import com.abdisalam.efleague.modal.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MatchUpRepository extends JpaRepository<Matchup, Long> {


    boolean existsByHomeTeamAndAwayTeamAndGameDateTime(
            Team homeTeam,
            Team awayTeam,
            LocalDateTime gameDateTime
    );


}
