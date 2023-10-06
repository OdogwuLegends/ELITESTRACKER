package com.capstoneproject.ElitesTracker.repositories;

import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance,Long> {
    Optional<Attendance> findByIpAddress(String ipAddress);
//    Optional<Attendance> findByIpAddressAndDateTaken(String ipAddress,String dateTaken);

    @Query("SELECT a FROM attendance a WHERE a.ipAddress = :ipAddress AND a.dateTaken = :dateTaken")
//@Query(
//        value = "SELECT * FROM attendance WHERE ip_address = :ipAddress AND date_taken = :dateTaken LIMIT 1",
//        nativeQuery = true
//)
    Optional<Attendance> findByIpAddressAndDateTaken(
            @Param("ipAddress") String ipAddress,
            @Param("dateTaken") String dateTaken
    );

    Optional<Attendance> findByCohort(String cohort);
    Optional<Attendance> findByUser(EliteUser eliteUser);

    @Query("SELECT a FROM attendance a WHERE a.ipAddressConcat = :ipAddress AND a.dateTaken = :dateTaken")
    Optional<Attendance> findByIpAddressConcatAndDateTaken(
            @Param("ipAddress") String ipAddressConcat,
            @Param("dateTaken") String dateTaken
    );

}
