package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.MockSemicolonDB;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MockSemicolonDbRepository extends JpaRepository<MockSemicolonDB,Long> {
    Optional<MockSemicolonDB> findBySemicolonEmail(String email);
}
