package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.exceptions.AttendanceAlreadyTakenException;
import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import com.capstoneproject.ElitesTracker.repositories.AttendanceRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.AttendanceService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.ATTENDANCE_ALREADY_TAKEN_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.App.*;

@Service
@AllArgsConstructor
public class EliteAttendanceService implements AttendanceService {
    private AttendanceRepository attendanceRepository;

    @Override
    public AttendanceResponse saveAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest, EliteUser eliteUser) {
        noAttendanceOnWeekendsCheck();

        AttendanceResponse response = new AttendanceResponse();
        String IpAddress = retrieveActualIP(httpServletRequest);
        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddress(IpAddress);

        if(foundAttendance.isPresent()){
            throw new AttendanceAlreadyTakenException(ATTENDANCE_ALREADY_TAKEN_EXCEPTION.getMessage());
        }else{
            buildNewAttendance(eliteUser, response, IpAddress);
        }
        return response;
    }

    private void buildNewAttendance(EliteUser eliteUser, AttendanceResponse response, String IpAddress) {
        Attendance newAttendance = new Attendance();
        newAttendance.setStatus(PRESENT);
        newAttendance.setIpAddress(IpAddress);
        newAttendance.setUser(eliteUser);
        attendanceRepository.save(newAttendance);
        response.setMessage(attendanceMessage(eliteUser.getFirstName()));
    }
}
