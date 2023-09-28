package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.EditAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.PermissionForAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.*;
import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.ABSENT;
import static com.capstoneproject.ElitesTracker.services.implementation.TestVariables.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.attendanceMessage;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.localDateToString;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.EDIT_STATUS_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class EliteAttendanceServiceTest {
    @Autowired
    private EliteAttendanceService eliteAttendanceService;
    @Autowired
    private EliteUserService eliteUserService;
    @Autowired
    private ElitesNativesService elitesNativesService;
    private UserRegistrationResponse response;
    private UserRegistrationResponse userRegistrationResponse;

    @Test
    void nativeCanTakeAttendance(){
       response = elitesNativesService.addNewNative(buildLegend());
       assertNotNull(response);
       userRegistrationResponse = eliteUserService.registerUser(buildLegendReg());
       assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);


        EliteUser foundUser = eliteUserService.findUserByEmail("l.odogwu@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(legendAttendance(),"172.16.0.70",foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage(foundUser.getFirstName()),attendanceResponse.getMessage());
    }

    @Test
    void nativeCannotTakeAttendanceOutsideSemicolonWifiNetwork(){
        response = elitesNativesService.addNewNative(buildCoutinho());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildCoutinhoReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("d.coutinho@native.semicolon.africa");
        assertThrows(DifferentWifiNetworkException.class,()-> eliteAttendanceService.saveAttendanceTest(coutinhoAttendance(),"1972.143.0.70",foundUser));
    }
//    @Test
//    void nativeCannotTakeAttendanceTwiceInADay(){
//        response = elitesNativesService.addNewNative(buildChiboy());
//        assertNotNull(response);
//        userRegistrationResponse = eliteUserService.registerUser(buildChiboyReg());
//        assertNotNull(userRegistrationResponse);
//
//        SetTimeRequest request = setTimeFrame();
//        eliteUserService.setTimeForAttendance(request);
//
//        EliteUser foundUser = eliteUserService.findUserByEmail("c.ugbo@native.semicolon.africa");
//        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(chiboyAttendance(),"172.16.0.71",foundUser);
//        assertNotNull(attendanceResponse);
//
//        assertThrows(AttendanceAlreadyTakenException.class,()-> eliteAttendanceService.saveAttendanceTest(chiboyAttendance(),"172.16.0.71",foundUser));
//    }
    @Test
    void nativeCanOnlyTakeAttendanceWithRegisteredDevice(){
        response = elitesNativesService.addNewNative(buildBlack());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildBlackReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("f.chiemela@native.semicolon.africa");
        assertThrows(NotSameDeviceException.class,()-> eliteAttendanceService.saveAttendanceTest(blackAttendance(),"172.16.0.72",foundUser));
    }
    @Test
    void nativeCannotTakeAttendanceIfPermissionIsDisabled(){
        response = elitesNativesService.addNewNative(buildInem());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildInemReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);
        eliteUserService.setAttendancePermissionForNative(disableInemPermission());

        EliteUser foundUser = eliteUserService.findUserByEmail("i.udousoro@native.semicolon.africa");
        assertThrows(NotPermittedForAttendanceException.class,()-> eliteAttendanceService.saveAttendanceTest(inemAttendance(),"172.16.0.73",foundUser));
    }
    @Test
    void nativeCannotTakeAttendanceIfTimeLimitNotSetByAdmin(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildWhiteReg());
        assertNotNull(userRegistrationResponse);
//
//        SetTimeRequest request = setTimeFrame();
//        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("f.nwadike@native.semicolon.africa");
        assertThrows(EntityDoesNotExistException.class,()-> eliteAttendanceService.saveAttendanceTest(whiteAttendance(),"172.16.0.74",foundUser));
    }
    @Test
    void adminCanFindAttendancesTaken(){
        List<Attendance> foundAttendances = eliteAttendanceService.findAllAttendances();
        assertNotNull(foundAttendances);
    }
    @Test
    void adminCanEditAttendanceStatusOfANative(){
        response = elitesNativesService.addNewNative(buildNed());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildNedReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("b.osisiogu@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(nedAttendance(),"172.16.0.75",foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage(foundUser.getFirstName()),attendanceResponse.getMessage());

        attendanceResponse = eliteAttendanceService.editAttendanceStatus(editNedAttendance(),foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(EDIT_STATUS_MESSAGE,attendanceResponse.getMessage());
    }


    private PermissionForAttendanceRequest disableInemPermission(){
        return PermissionForAttendanceRequest.builder()
                .nativeSemicolonEmail("i.udousoro@native.semicolon.africa")
                .cohort("15")
                .permission(DISABLED)
                .build();
    }
    private EditAttendanceRequest editNedAttendance(){
        return EditAttendanceRequest.builder()
                .nativeSemicolonEmail("b.osisiogu@native.semicolon.africa")
                .cohort("15")
                .attendanceStatus(ABSENT)
                .date(localDateToString(LocalDate.now()))
                .build();
    }
}