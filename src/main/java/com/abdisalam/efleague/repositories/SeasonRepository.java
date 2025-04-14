package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeasonRepository extends JpaRepository<Season, Long> {
    Optional<Season> findByName(String name);
    List<Season> findByActive(boolean active);

    Optional<Season> findByActiveTrue();
}
