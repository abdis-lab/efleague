package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Matchup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchUpRepository extends JpaRepository<Matchup, Long> {





}
