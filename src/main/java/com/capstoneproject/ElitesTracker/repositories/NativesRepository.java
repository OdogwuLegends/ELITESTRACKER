package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.Natives;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NativesRepository extends JpaRepository<Natives,Long> {
    Optional<Natives> findBySemicolonEmail(String email);
    Optional<Natives> findByCohort(String cohort);
    Optional<Natives> findBySemicolonEmailAndSemicolonID(String email, String scv);
    boolean existsBySemicolonEmailAndSemicolonID(String email, String scv);
}
