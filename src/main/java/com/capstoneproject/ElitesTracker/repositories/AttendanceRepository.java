package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findByIpAddress(String ipAddress);
    Optional<Attendance> findByCohort(String cohort);
    Optional<Attendance> findByUser(EliteUser eliteUser);

}
