package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
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
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.services.implementation.TestVariables.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
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
    void nativeCanTakeNormalAttendance(){
       response = elitesNativesService.addNewNative(buildLegend());
       assertNotNull(response);
       userRegistrationResponse = eliteUserService.registerUser(buildLegendReg());
       assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);


        EliteUser foundUser = eliteUserService.findUserByEmail("l.odogwu@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(legendAttendance(),"172.16.0.70",foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(normalAttendanceMessage(foundUser.getFirstName()),attendanceResponse.getMessage());
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
    @Test
    void nativeCannotTakeAttendanceTwiceInADay(){
        response = elitesNativesService.addNewNative(buildChiboy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChiboyReg());
        assertNotNull(userRegistrationResponse);


        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser chiboy = eliteUserService.findUserByEmail("c.ugbo@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(chiboyAttendance(),"172.16.0.71",chiboy);
        assertNotNull(attendanceResponse);

        assertThrows(AttendanceAlreadyTakenException.class,()-> eliteAttendanceService.saveAttendanceTest(chiboyAttendance(),"172.16.0.71",chiboy));

    }
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
        assertThrows(NotPermittedException.class,()-> eliteAttendanceService.saveAttendanceTest(inemAttendance(),"172.16.0.73",foundUser));
    }
    @Test
    void nativeCannotTakeAttendanceIfTimeLimitNotSetByAdmin(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildWhiteReg());
        assertNotNull(userRegistrationResponse);


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
        assertEquals(normalAttendanceMessage(foundUser.getFirstName()),attendanceResponse.getMessage());

        attendanceResponse = eliteAttendanceService.editAttendanceStatus(editNedAttendance(),foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(EDIT_STATUS_MESSAGE,attendanceResponse.getMessage());
    }
    @Test
    void oneDeviceCannotTakeAttendanceTwiceInADay(){
        response = elitesNativesService.addNewNative(buildJide());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildJideReg());
        assertNotNull(userRegistrationResponse);

        response = elitesNativesService.addNewNative(buildOluchi());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildOluchiReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser jide = eliteUserService.findUserByEmail("b.farinde@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(jideAttendance(),"172.16.0.76",jide);
        assertNotNull(attendanceResponse);

        EliteUser oluchi = eliteUserService.findUserByEmail("o.duru@native.semicolon.africa");
        assertThrows(AttendanceAlreadyTakenException.class,()-> eliteAttendanceService.saveAttendanceTest(oluchiAttendance(),"172.16.0.76",oluchi));
    }
    @Test
    void throwErrorIfIpAddressIsNull(){
        assertThrows(NoInternetException.class,()-> eliteAttendanceService.saveAttendanceTest(new AttendanceRequest(),null,new EliteUser()));
    }
    @Test
    void throwErrorIfIpAddressIsEmptyString(){
        assertThrows(NoInternetException.class,()-> eliteAttendanceService.saveAttendanceTest(new AttendanceRequest(),"",new EliteUser()));
    }
    @Test
    void attemptToBackdateForAttendanceThrowsError(){
        response = elitesNativesService.addNewNative(buildKinzy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildKinzyReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser kinzy = eliteUserService.findUserByEmail("s.lawal@native.semicolon.africa");
        assertThrows(NotPermittedException.class,()-> eliteAttendanceService.saveAttendanceTest(kinzyAttendance(),"172.16.0.77",kinzy));
    }
    @Test
    void attemptToTakeAttendanceForFutureDateThrowsError(){
        response = elitesNativesService.addNewNative(buildMalik());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildMalikReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser malik = eliteUserService.findUserByEmail("a.alhaji@native.semicolon.africa");
        assertThrows(NotPermittedException.class,()-> eliteAttendanceService.saveAttendanceTest(malikAttendance(),"172.16.0.78",malik));
    }
    @Test
    void setStatusToAbsentIfNotPresent(){
        response = elitesNativesService.addNewNative(buildTimi());
        assertNotNull(response);
        response = elitesNativesService.addNewNative(buildOla());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildTimiReg());
        assertNotNull(userRegistrationResponse);
        userRegistrationResponse = eliteUserService.registerUser(buildOlaReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser timi = eliteUserService.findUserByEmail("t.leyin@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(timiAttendance(),"172.16.0.79",timi);
        assertNotNull(attendanceResponse);

        List<EliteUser> allNatives = eliteUserService.findAllNatives();
        attendanceResponse = eliteAttendanceService.setToAbsent(allNatives);
        assertNotNull(attendanceResponse);
        assertEquals("Execution Completed",attendanceResponse.getMessage());

        List<Attendance> allAttendances = eliteAttendanceService.findAllAttendances();
        boolean isAbsent = false;
        for (Attendance allAttendance : allAttendances) {
            if (allAttendance.getStatus().equals(ABSENT)) {
                isAbsent = true;
                break;
            }
        }
        assertTrue(isAbsent);
    }
//    @Test
//    void nativeCanTakeTardyAttendance(){
//        //This test case will fail between the hours of 17:01 and 00:59
//        response = elitesNativesService.addNewNative(buildBolaji());
//        assertNotNull(response);
//        userRegistrationResponse = eliteUserService.registerUser(buildBolajiReg());
//        assertNotNull(userRegistrationResponse);
//
//        SetTimeRequest request = setTimeFrameForTardyAttendance();
//        eliteUserService.setTimeForAttendance(request);
//
//        EliteUser bolaji = eliteUserService.findUserByEmail("b.jibowu@native.semicolon.africa");
//        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(bolajiAttendance(),"172.16.0.80",bolaji);
//        assertNotNull(attendanceResponse);
//        assertEquals(tardyAttendanceMessage(bolaji.getFirstName()),attendanceResponse.getMessage());
//        assertEquals("Hi BOLAJI, Attendance taken. But your are late today!",attendanceResponse.getMessage());
//    }


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