package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.EditAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface AttendanceService {
    AttendanceResponse saveAttendance(AttendanceRequest request,EliteUser eliteUser);
    AttendanceResponse saveAttendanceTest(AttendanceRequest request, String Ip, EliteUser eliteUser);
    AttendanceResponse editAttendanceStatus(EditAttendanceRequest request, EliteUser foundUser);
    List<Attendance> findAllAttendances();
}
