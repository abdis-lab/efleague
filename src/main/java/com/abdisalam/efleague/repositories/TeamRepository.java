package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
