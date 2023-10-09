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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.*;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

@Service
@AllArgsConstructor
@Slf4j
public class EliteAttendanceService implements AttendanceService {
    private final com.capstoneproject.ElitesTracker.repositories.AttendanceRepository attendanceRepository;
    private final TimeEligibilityService timeEligibilityService;


    @Override
    public AttendanceResponse saveAttendance(AttendanceRequest request, EliteUser eliteUser) {
        noAttendanceOnWeekendsCheck();

        if(request.getIpAddress() == null || request.getIpAddress().equals(EMPTY_STRING)){
            throw new NoInternetException(NETWORK_ERROR_EXCEPTION.getMessage());
        }
        if(!request.getAttendanceDate().equals(getCurrentDateForAttendance())){
            throw new NotPermittedException(WRONG_DATE_FOR_ATTENDANCE_EXCEPTION.getMessage());
        }

//        if(!subStringRealIp(request.getIpAddress()).equals(REAL_BASE_IP_ADDRESS)){
//            throw new DifferentWifiNetworkException(DIFFERENT_NETWORK_EXCEPTION.getMessage());
//        }

        if(!subStringRealIp(request.getIpAddress()).equals(PERSONAL_BASE_IP_ADDRESS)){
            throw new DifferentWifiNetworkException(DIFFERENT_NETWORK_EXCEPTION.getMessage());
        }

        AttendanceResponse response = new AttendanceResponse();
//        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddress(request.getIpAddress());
//        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddressAndDateTaken(request.getIpAddress(),getCurrentDateForAttendance());
        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddressConcatAndDateTaken(request.getIpAddressConcat(),getCurrentDateForAttendance());

        if((foundAttendance.isPresent())){
            throw new AttendanceAlreadyTakenException(ATTENDANCE_ALREADY_TAKEN_EXCEPTION.getMessage());
        } else if (isAnotherDevice(request,eliteUser)) {
            throw new NotSameDeviceException(DIFFERENT_DEVICE_EXCEPTION.getMessage());
        } else if (eliteUser.getPermission().equals(DISABLED)) {
            throw new NotPermittedException(NATIVE_NOT_PERMITTED_FOR_ATTENDANCE_EXCEPTION.getMessage());
        } else{
            checkTimeFrameAndBuildAttendance(eliteUser, response, request);
        }
        return response;
    }

    @Override
    public AttendanceResponse saveAttendanceTest(AttendanceRequest request, String IpAddress, EliteUser eliteUser) {
//        noAttendanceOnWeekendsCheck();

        if(IpAddress == null || IpAddress.equals(EMPTY_STRING)){
            throw new NoInternetException(NETWORK_ERROR_EXCEPTION.getMessage());
        }

        if(!subStringTestIp(IpAddress).equals(TEST_BASE_IP_ADDRESS)){
            throw new DifferentWifiNetworkException(DIFFERENT_NETWORK_EXCEPTION.getMessage());
        }

        if(!request.getAttendanceDate().equals(getCurrentDateForAttendance())){
            throw new NotPermittedException(WRONG_DATE_FOR_ATTENDANCE_EXCEPTION.getMessage());
        }

        AttendanceResponse response = new AttendanceResponse();
        Optional<Attendance> foundAttendance =  attendanceRepository.findByIpAddressConcatAndDateTaken(request.getIpAddressConcat(),getCurrentDateForAttendance());

        if((foundAttendance.isPresent())){
            throw new AttendanceAlreadyTakenException(ATTENDANCE_ALREADY_TAKEN_EXCEPTION.getMessage());
        } else if (isAnotherDevice(request,eliteUser)) {
            throw new NotSameDeviceException(DIFFERENT_DEVICE_EXCEPTION.getMessage());
        } else if (eliteUser.getPermission().equals(DISABLED)) {
            throw new NotPermittedException(NATIVE_NOT_PERMITTED_FOR_ATTENDANCE_EXCEPTION.getMessage());
        } else{
            checkTimeFrameAndBuildAttendance(eliteUser, response, request);
        }
        return response;
    }

    @Override
    public AttendanceResponse editAttendanceStatus(EditAttendanceRequest request,EliteUser foundUser) {

        Optional<Attendance> foundAttendance = attendanceRepository.findByUser(foundUser);

        if(foundAttendance.isEmpty() || !stringDateToString(request.getDate()).equals(subStringDate(foundAttendance.get().getDateTaken()))){
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

    @Override
    public AttendanceResponse setToAbsent(List<EliteUser> allNatives) {
        List<Attendance> allAttendances = findAllAttendances();
        List<Long> listOfAttendanceIds = new ArrayList<>();

        for (Attendance takenAttendance : allAttendances) {
            Long id = takenAttendance.getUser().getId();
            listOfAttendanceIds.add(id);
        }

        for (EliteUser allNative : allNatives) {
            if (!listOfAttendanceIds.contains(allNative.getId())) {
                Attendance newAttendance = new Attendance();
                newAttendance.setStatus(ABSENT);
                newAttendance.setIpAddress(EMPTY_STRING);
                newAttendance.setIpAddressConcat(EMPTY_STRING);
                newAttendance.setUser(allNative);
                newAttendance.setCohort(allNative.getCohort());
                attendanceRepository.save(newAttendance);
            }
        }
        AttendanceResponse response = new AttendanceResponse();
        response.setMessage(EXECUTION_COMPLETED);
        return response;
    }

    private void checkTimeFrameAndBuildAttendance(EliteUser eliteUser, AttendanceResponse response,AttendanceRequest request){
        List<TimeEligibility> timeFrames = timeEligibilityService.findAllTimeFrames();

        if(timeFrames.isEmpty()){
            throw new EntityDoesNotExistException(NO_TIME_LIMITS_SET_EXCEPTION.getMessage());
        }

        TimeEligibility timeEligibility = timeFrames.get(0);
//        LocalTime currentTime = LocalTime.now();
        ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
//        LocalTime startTime = LocalTime.of(timeEligibility.getStartHour(),timeEligibility.getStartMinute());
        ZonedDateTime startTime = currentTime.withHour(timeEligibility.getStartHour()).withMinute(timeEligibility.getStartMinute()).withSecond(0).withNano(0);
//        LocalTime endTime = LocalTime.of(timeEligibility.getEndHour(),timeEligibility.getEndMinute());
        ZonedDateTime endTime = currentTime.withHour(timeEligibility.getEndHour()).withMinute(timeEligibility.getEndMinute()).withSecond(0).withNano(0);



//        LocalTime baseTime = LocalTime.of(23,59);
        ZonedDateTime baseTime = currentTime.withHour(17).withMinute(0).withSecond(0).withNano(0);

        if(currentTime.isBefore(startTime) && currentTime.isBefore(endTime)){
            throw new TimeLimitException(beforeAttendanceMessage(zonedTimeToString(startTime)));
        }
        if (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)){
            buildNormalAttendance(eliteUser, response, request);
        }
        if(currentTime.isAfter(endTime) && currentTime.isBefore(baseTime)){
            buildTardyAttendance(eliteUser, response, request);
        }
        if(currentTime.isAfter(baseTime) && currentTime.isBefore(startTime)){
            String start = zonedTimeToString(startTime);
            throw new TimeLimitException(afterAttendanceMessage(zonedTimeToString(baseTime),start));
        }
    }

    private void buildNormalAttendance(EliteUser eliteUser, AttendanceResponse response, AttendanceRequest request) {
        Attendance newAttendance = new Attendance();
        newAttendance.setStatus(PRESENT);
        newAttendance.setIpAddress(request.getIpAddress());
        newAttendance.setIpAddressConcat(request.getIpAddressConcat());
        newAttendance.setUser(eliteUser);
        newAttendance.setCohort(eliteUser.getCohort());
        attendanceRepository.save(newAttendance);
        response.setMessage(normalAttendanceMessage(eliteUser.getFirstName()));
    }
    private void buildTardyAttendance(EliteUser eliteUser, AttendanceResponse response, AttendanceRequest request) {
        Attendance newAttendance = new Attendance();
        newAttendance.setStatus(TARDY);
        newAttendance.setIpAddress(request.getIpAddress());
        newAttendance.setIpAddressConcat(request.getIpAddressConcat());
        newAttendance.setUser(eliteUser);
        newAttendance.setCohort(eliteUser.getCohort());
        attendanceRepository.save(newAttendance);
        response.setMessage(tardyAttendanceMessage(eliteUser.getFirstName()));
    }
//    private boolean isAnotherDevice(AttendanceRequest request,EliteUser eliteUser){
//        return !eliteUser.getScreenWidth().equals(request.getScreenHeight()) ||
//                !eliteUser.getScreenHeight().equals(request.getScreenWidth());
//    }
    private boolean isAnotherDevice(AttendanceRequest request,EliteUser eliteUser){
        return !eliteUser.getScreenWidth().equals(request.getScreenWidth()) ||
                !eliteUser.getScreenHeight().equals(request.getScreenHeight());
    }

    private void timeTrial() {

//        String myTime = "07:06";
//        int hour = 0;
//        int minute = 0;
//
//        String[] timeParts = myTime.split(":");
//
//        if (timeParts.length == 2) {
//            String hourStr = timeParts[0];
//            String minuteStr = timeParts[1];
//
//            hour = Integer.parseInt(hourStr);
//            minute = Integer.parseInt(minuteStr);
//
//            if (hourStr.length() > 0 && hourStr.charAt(0) == '0') {
//                hour = Integer.parseInt(hourStr.substring(1));
//            }
//            if (minuteStr.length() > 1 && minuteStr.charAt(0) == '0' && minuteStr.charAt(1) > '8') {
//                minute = Character.getNumericValue(minuteStr.charAt(1));
//            }
//        }
//
//        System.out.println("Hour: " + hour);
//        System.out.println("Minute: " + minute);
    }
}
