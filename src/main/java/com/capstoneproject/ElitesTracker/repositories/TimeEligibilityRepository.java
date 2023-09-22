package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.TimeEligibility;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TimeEligibilityRepository extends JpaRepository<TimeEligibility,Long> {

}
