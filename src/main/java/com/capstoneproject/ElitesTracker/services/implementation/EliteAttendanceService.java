package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.EditAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.exceptions.*;
import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import com.capstoneproject.ElitesTracker.models.TimeEligibility;
import com.capstoneproject.ElitesTracker.services.interfaces.AttendanceService;
import com.capstoneproject.ElitesTracker.services.interfaces.TimeEligibilityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.BASE_IP_ADDRESS;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.EDIT_STATUS_MESSAGE;

@Service
@AllArgsConstructor
@Slf4j
public class EliteAttendanceService implements AttendanceService {
    private final com.capstoneproject.ElitesTracker.repositories.AttendanceRepository attendanceRepository;
    private final TimeEligibilityService timeEligibilityService;

    @Override
    public AttendanceResponse saveAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest, EliteUser eliteUser) {
        noAttendanceOnWeekendsCheck();

        AttendanceResponse response = new AttendanceResponse();
        String IpAddress = retrieveActualIP(httpServletRequest);

        if(!subStringIp(IpAddress).equals(BASE_IP_ADDRESS)){
            throw new DifferentWifiNetworkException(DIFFERENT_NETWORK_EXCEPTION.getMessage());
        }

        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddress(IpAddress);

        if((foundAttendance.isPresent()) && (subStringDate(foundAttendance.get().getDate()).equals(localDateTodayToString()))){
            throw new AttendanceAlreadyTakenException(ATTENDANCE_ALREADY_TAKEN_EXCEPTION.getMessage());
        } else if (isAnotherDevice(request,eliteUser)) {
            throw new NotSameDeviceException(DIFFERENT_DEVICE_EXCEPTION.getMessage());
        } else if (eliteUser.getPermission().equals(DISABLED)) {
            throw new NotPermittedForAttendanceException(NATIVE_NOT_PERMITTED_FOR_ATTENDANCE_EXCEPTION.getMessage());
        } else{
            checkTimeFrameAndBuildAttendance(eliteUser, response, IpAddress);
        }
        return response;
    }

    @Override
    public AttendanceResponse saveAttendanceTest(AttendanceRequest request, String IpAddress, EliteUser eliteUser) {
//        noAttendanceOnWeekendsCheck();

        if(!subStringIp(IpAddress).equals("172.16.0.")){
            throw new DifferentWifiNetworkException(DIFFERENT_NETWORK_EXCEPTION.getMessage());
        }

        AttendanceResponse response = new AttendanceResponse();

        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddress(IpAddress);

        if((foundAttendance.isPresent()) && (subStringDate(foundAttendance.get().getDate()).equals(localDateTodayToString()))){
            throw new AttendanceAlreadyTakenException(ATTENDANCE_ALREADY_TAKEN_EXCEPTION.getMessage());
        } else if (isAnotherDevice(request,eliteUser)) {
            throw new NotSameDeviceException(DIFFERENT_DEVICE_EXCEPTION.getMessage());
        } else if (eliteUser.getPermission().equals(DISABLED)) {
            throw new NotPermittedForAttendanceException(NATIVE_NOT_PERMITTED_FOR_ATTENDANCE_EXCEPTION.getMessage());
        } else{
            checkTimeFrameAndBuildAttendance(eliteUser, response, IpAddress);
        }
        return response;
    }

    @Override
    public AttendanceResponse editAttendanceStatus(EditAttendanceRequest request,EliteUser foundUser) {

        Optional<Attendance> foundAttendance = attendanceRepository.findByUser(foundUser);

        if(foundAttendance.isEmpty() || !stringDateToString(request.getDate()).equals(subStringDate(foundAttendance.get().getDate()))){
            throw new RecordNotFoundException(RECORD_NOT_FOUND_EXCEPTION.getMessage());
        }
        if(!foundAttendance.get().getCohort().equals(request.getCohort())){
            throw new EntityDoesNotExistException(nativeNotFoundMessage(request.getCohort()));
        }

        Attendance attendanceToEdit = foundAttendance.get();
        attendanceToEdit.setStatus(request.getAttendanceStatus());
        attendanceRepository.save(attendanceToEdit);
        AttendanceResponse response = new AttendanceResponse();
        response.setMessage(EDIT_STATUS_MESSAGE);
        return response;
    }

    @Override
    public List<Attendance> findAllAttendances() {
        return attendanceRepository.findAll();
    }

    private void checkTimeFrameAndBuildAttendance(EliteUser eliteUser, AttendanceResponse response, String IpAddress){
        List<TimeEligibility> timeFrames = timeEligibilityService.findAllTimeFrames();

        if(timeFrames.isEmpty()){
            throw new EntityDoesNotExistException(NO_TIME_LIMITS_SET_EXCEPTION.getMessage());
        }

        TimeEligibility timeEligibility = timeFrames.get(0);
        LocalTime startTime = LocalTime.of(timeEligibility.getStartHour(),timeEligibility.getStartMinute());
        LocalTime endTime = LocalTime.of(timeEligibility.getEndHour(),timeEligibility.getEndMinute());
        LocalTime currentTime = LocalTime.now();
        LocalTime baseTime = LocalTime.of(23,59);

        if(currentTime.isBefore(startTime) && currentTime.isBefore(endTime)){
            throw new TimeLimitException(beforeAttendanceMessage(localTimeToString(startTime)));
        }
        if(currentTime.isAfter(endTime) && currentTime.isBefore(baseTime)){
            String end = localTimeToString(endTime);
            String start = localTimeToString(startTime);
            throw new TimeLimitException(afterAttendanceMessage(end,start));
        }
        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)){
            buildNewAttendance(eliteUser, response, IpAddress);
        }
    }

    private void buildNewAttendance(EliteUser eliteUser, AttendanceResponse response, String IpAddress) {
        Attendance newAttendance = new Attendance();
        newAttendance.setStatus(PRESENT);
        newAttendance.setIpAddress(IpAddress);
        newAttendance.setUser(eliteUser);
        newAttendance.setCohort(eliteUser.getCohort());
        attendanceRepository.save(newAttendance);
        response.setMessage(attendanceMessage(eliteUser.getFirstName()));
    }
    private boolean isAnotherDevice(AttendanceRequest request,EliteUser eliteUser){
        return !eliteUser.getScreenWidth().equals(request.getScreenWidth()) || !eliteUser.getScreenHeight().equals(request.getScreenHeight());
    }
}
