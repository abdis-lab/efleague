package com.abdisalam.efleague.repositories;

import com.abdisalam.efleague.modal.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);

    Optional<User> findByRole(User.Role role);

    @Query("SELECT u FROM User u WHERE u.team IS NULL AND u.role = 'ROLE_PLAYER'")
    List<User> findByTeamIsNull();
}
