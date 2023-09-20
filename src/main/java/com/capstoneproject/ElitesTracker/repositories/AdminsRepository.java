package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.Admins;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminsRepository extends JpaRepository<Admins,Long> {
    Optional<Admins> findBySemicolonEmail(String email);
    boolean existsBySemicolonEmail(String email);
}
