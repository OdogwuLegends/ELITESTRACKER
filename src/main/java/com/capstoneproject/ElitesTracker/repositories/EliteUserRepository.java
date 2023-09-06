package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.EliteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EliteUserRepository extends JpaRepository<EliteUser,Long> {
    Optional<EliteUser> findBySemicolonEmail(String email);
}
