package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.EditAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
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
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.ABSENT;
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
            checkTimeFrameAndBuildAttendance(eliteUser, response, IpAddress, request);
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
            checkTimeFrameAndBuildAttendance(eliteUser, response, IpAddress, request);
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

    private void checkTimeFrameAndBuildAttendance(EliteUser eliteUser, AttendanceResponse response, String IpAddress, AttendanceRequest request){
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
            buildNewAttendance(eliteUser, response, IpAddress, request);
        }
    }

    private void buildNewAttendance(EliteUser eliteUser, AttendanceResponse response, String IpAddress, AttendanceRequest request) {
        Attendance newAttendance = new Attendance();
        newAttendance.setStatus(convertToEnum(request.getAttendanceStatus()));
        newAttendance.setIpAddress(IpAddress);
        newAttendance.setUser(eliteUser);
        newAttendance.setCohort(eliteUser.getCohort());
        attendanceRepository.save(newAttendance);
        response.setMessage(attendanceMessage(eliteUser.getFirstName()));
    }
    private boolean isAnotherDevice(AttendanceRequest request,EliteUser eliteUser){
        return !eliteUser.getScreenWidth().equals(request.getScreenWidth()) || !eliteUser.getScreenHeight().equals(request.getScreenHeight());
    }
    private AttendanceStatus convertToEnum(String value){
        if(value.equals("PRESENT")){
            return PRESENT;
        } else if ((value.equals("ABSENT"))) {
            return ABSENT;
        }
        throw new RuntimeException("Invalid value");
    }

    public static void main(String[] args) {

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

//        ZonedDateTime customDateTime = ZonedDateTime.of(2023, 9, 26, 14, 30, 0, 0, ZoneId.of("Africa/Lagos"));
//
//        // Define a DateTimeFormatter to format the output (date and time)
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//
//        // Format the ZonedDateTime and print it
//        String formattedDateTime = customDateTime.format(formatter);
////        System.out.println("Custom Date and Time in West Africa (Lagos): " + formattedDateTime);
//        System.out.println(formattedDateTime);

//        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));
//
//        // Extract the LocalTime component (hour, minute, second, and nanosecond)
//        int hour = currentDateTime.getHour();
//        int minute = currentDateTime.getMinute();
//        int second = currentDateTime.getSecond();
//
//
//        System.out.println("Current Time in West Africa (Lagos): " +
//                String.format("%02d:%02d:%02d", hour, minute, second));

        ZonedDateTime currentDateTime = ZonedDateTime.now(ZoneId.of("Africa/Lagos"));

        // Define a specific time (e.g., 14:30) for comparison
        int specificHour = 14;
        int specificMinute = 30;

        // Create a ZonedDateTime for the specific time in the same time zone
        ZonedDateTime specificDateTime = currentDateTime.withHour(specificHour).withMinute(specificMinute).withSecond(0).withNano(0);

        // Compare the specific time to the current time
        if (specificDateTime.isBefore(currentDateTime)) {
            System.out.println("The specific time is before the current time.");
        } else if (specificDateTime.isEqual(currentDateTime)) {
            System.out.println("The specific time is equal to the current time.");
        } else {
            System.out.println("The specific time is after the current time.");
        }
    }
}
