package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.LoginResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
import com.capstoneproject.ElitesTracker.services.interfaces.NativesService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.attendanceMessage;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.welcomeMessage;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.LOGIN_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EliteUserServiceTest {
    @Autowired
    private EliteUserService eliteUserService;
    @Autowired
    private ElitesNativesService elitesNativesService;
    @Autowired
    private EliteAdminService eliteAdminService;
    private UserRegistrationResponse response;
    private UserRegistrationResponse userRegistrationResponse;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private NativesService nativesService;



    @Test
    void registerUser() {
        response = eliteAdminService.addNewAdmin(buildPatience());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildFirstUser());
        assertNotNull(userRegistrationResponse);
        assertEquals(welcomeMessage("PATIENCE"),userRegistrationResponse.getMessage());
    }

    @Test
    void loginUser() {
        response = elitesNativesService.addNewNative(buildLegend());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildSecondUser());
        assertNotNull(userRegistrationResponse);

        LoginResponse loginResponse = eliteUserService.loginUser(buildLoginRequest());
        assertNotNull(loginResponse);
        assertTrue(loginResponse.isLoggedIn());
        assertEquals(LOGIN_MESSAGE, loginResponse.getMessage());
    }
    @Test
    void loginUserWithWrongEmailThrowsException(){
        response = eliteAdminService.addNewAdmin(buildGabriel());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildThirdUser());
        assertNotNull(userRegistrationResponse);

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongEmail()));
    }
    @Test
    void loginUserWithWrongPasswordThrowsException(){
        response = eliteAdminService.addNewAdmin(buildNewGuy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildFourthUser());
        assertNotNull(userRegistrationResponse);

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongPassword()));
    }
    @Test
    void nativeCanTakeAttendance(){
       response = elitesNativesService.addNewNative(buildChiboy());
       assertNotNull(response);
       userRegistrationResponse = eliteUserService.registerUser(buildFifthUser());
       assertNotNull(userRegistrationResponse);

        AttendanceResponse attendanceResponse = eliteUserService.takeAttendance(fillAttendanceDetails(),httpServletRequest);
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage("Chindeu"),attendanceResponse.getMessage());
    }

    private UserRegistrationRequest buildFirstUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("patience@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildSecondUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .password("odogwu123")
                .scv("scv15008")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildThirdUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("gabriel@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildFourthUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("newPassword")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildFifthUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15009")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AddAdminRequest buildPatience(){
        return AddAdminRequest.builder()
                .firstName("Patience")
                .lastName("Pat")
                .semicolonEmail("patience@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildGabriel(){
        return AddAdminRequest.builder()
                .firstName("Gabriel")
                .lastName("Gab")
                .semicolonEmail("gabriel@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildNewGuy(){
        return AddAdminRequest.builder()
                .firstName("NewGuy")
                .lastName("Guy")
                .semicolonEmail("newguy@semicolon.africa")
                .build();
    }
    private AddNativeRequest buildLegend(){
        return AddNativeRequest.builder()
                .firstName("Odogwu")
                .lastName("Legend")
                .cohort("15")
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .semicolonID("SCV15008")
                .build();
    }
    private AddNativeRequest buildChiboy(){
        return AddNativeRequest.builder()
                .firstName("Chindeu")
                .lastName("Ugbo")
                .cohort("15")
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .semicolonID("SCV15009")
                .build();
    }
    private LoginRequest buildLoginRequest(){
        return LoginRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .password("odogwu123")
                .build();
    }
    private LoginRequest buildLoginRequestWithWrongEmail(){
        return LoginRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("password")
                .build();
    }
    private LoginRequest buildLoginRequestWithWrongPassword(){
        return LoginRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("adminPassword")
                .build();
    }

    private AttendanceRequest fillAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
}