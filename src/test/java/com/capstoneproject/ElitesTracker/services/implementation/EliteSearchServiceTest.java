package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.SearchRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceSheetResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.RecordNotFoundException;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.capstoneproject.ElitesTracker.services.implementation.TestVariables.*;
import static com.capstoneproject.ElitesTracker.services.implementation.TestVariables.legendAttendance;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.attendanceMessage;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.localDateToString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("dev")
class EliteSearchServiceTest {
    @Autowired
    private EliteAttendanceService eliteAttendanceService;
    @Autowired
    private EliteUserService eliteUserService;
    @Autowired
    private ElitesNativesService elitesNativesService;
    @Autowired
    private EliteSearchService eliteSearchService;
    private UserRegistrationResponse response;
    private UserRegistrationResponse userRegistrationResponse;

    @Test
    void nativeCanGenerateAttendanceReportForSelf(){
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

        List<AttendanceSheetResponse> attendanceLog = eliteSearchService.searchAttendanceReportForSelf(legendAttendanceReport(),foundUser);
        assertThat(attendanceLog).isNotNull();
    }
    @Test
    void adminCanGenerateAttendanceReportForNative(){
        response = elitesNativesService.addNewNative(buildChiboy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChiboyReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);


        EliteUser foundUser = eliteUserService.findUserByEmail("c.ugbo@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(chiboyAttendance(),"172.16.0.71",foundUser);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage(foundUser.getFirstName()),attendanceResponse.getMessage());

        List<AttendanceSheetResponse> attendanceLog = eliteSearchService.searchAttendanceReportForSelf(chiboyAttendanceReport(),foundUser);
        assertThat(attendanceLog).isNotNull();
    }
    @Test
    void adminCanGenerateAttendanceReportForCohort(){
        response = elitesNativesService.addNewNative(buildCoutinho());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildCoutinhoReg());
        assertNotNull(userRegistrationResponse);

        response = elitesNativesService.addNewNative(buildNed());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildNedReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser firstUser = eliteUserService.findUserByEmail("d.coutinho@native.semicolon.africa");
        AttendanceResponse attendanceResponse = eliteAttendanceService.saveAttendanceTest(coutinhoAttendance(),"172.16.0.72",firstUser);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage(firstUser.getFirstName()),attendanceResponse.getMessage());

        EliteUser secondUser = eliteUserService.findUserByEmail("b.osisiogu@native.semicolon.africa");
        attendanceResponse = eliteAttendanceService.saveAttendanceTest(nedAttendance(),"172.16.0.73",secondUser);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage(secondUser.getFirstName()),attendanceResponse.getMessage());

        List<AttendanceSheetResponse> attendanceLog = eliteSearchService.searchAttendanceReportForCohort(cohort15AttendanceReport());
        assertThat(attendanceLog).isNotNull();
    }
    @Test
    void throwErrorIfNoAttendanceRecordsFoundForSelfSearch(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildWhiteReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("f.nwadike@native.semicolon.africa");
        assertThrows(RecordNotFoundException.class,()-> eliteSearchService.searchAttendanceReportForSelf(whiteAttendanceReport(),foundUser));
    }
    @Test
    void throwErrorIfNoAttendanceRecordsFoundForSelfNative(){
        response = elitesNativesService.addNewNative(buildJide());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildJideReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendance(request);

        EliteUser foundUser = eliteUserService.findUserByEmail("b.farinde@native.semicolon.africa");
        assertThrows(RecordNotFoundException.class,()-> eliteSearchService.searchAttendanceReportForNative(jideAttendanceReport(),foundUser));
    }
    @Test
    void throwErrorIfNoAttendanceRecordsFoundForSelfCohort(){
        assertThrows(RecordNotFoundException.class,()-> eliteSearchService.searchAttendanceReportForCohort(cohort14AttendanceReport()));
    }


    private SearchRequest legendAttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest chiboyAttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest whiteAttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest jideAttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("b.farinde@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest cohort15AttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .cohort("15")
                .build();
    }
    private SearchRequest cohort14AttendanceReport(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .cohort("14")
                .build();
    }
}