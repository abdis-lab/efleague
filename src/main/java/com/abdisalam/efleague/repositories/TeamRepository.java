package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Team;
import com.abdisalam.efleague.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByName(String name);
    Optional<Team> findByCaptain(User captain);

}
