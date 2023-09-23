package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.LoginResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.AdminsNotPermittedException;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
import com.capstoneproject.ElitesTracker.exceptions.UserExistsException;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.ABSENT;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.attendanceMessage;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.welcomeMessage;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.EDIT_STATUS_MESSAGE;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.LOGIN_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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



    @Test
    void registerUser() {
        response = eliteAdminService.addNewAdmin(buildPatience());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildFirstUser());
        assertNotNull(userRegistrationResponse);
        assertEquals(welcomeMessage("PATIENCE"),userRegistrationResponse.getMessage());
    }
    @Test
    void registerUserThrowsErrorIfUserExists(){
        assertThrows(UserExistsException.class,()-> eliteUserService.registerUser(buildFirstUser()));
    }
    @Test
    void loginUserWithCorrectionDetails() {
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

        setTimeFrame();

        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(firstAttendanceDetails(),"172.16.0.70");
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage("CHINEDU"),attendanceResponse.getMessage());
    }
    @Test
    void adminCannotTakeAttendance(){
        response = eliteAdminService.addNewAdmin(buildChibuzo());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildEightUser());
        assertNotNull(userRegistrationResponse);
        setTimeFrame();

        assertThrows(AdminsNotPermittedException.class,()-> eliteUserService.takeAttendanceTest(thirdAttendanceDetails(),"172.16.0.71"));
    }

    @Test
    void adminCanEditAttendanceStatusOfNative(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildSeventhUser());
        assertNotNull(userRegistrationResponse);

        setTimeFrame();
        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(secondAttendanceDetails(),"172.16.0.73");
        assertThat(attendanceResponse).isNotNull();

        EditAttendanceRequest request = EditAttendanceRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .cohort("15")
                .date("23/09/2023")
                .attendanceStatus(ABSENT)
                .build();
        attendanceResponse = eliteUserService.editAttendanceStatus(request);
        assertThat(attendanceResponse).isNotNull();
        assertEquals(EDIT_STATUS_MESSAGE,attendanceResponse.getMessage());
    }



    @Test
    void findUserByCorrectEmail(){
        EliteUser foundUser = eliteUserService.findUserByEmail("patience@semicolon.africa");
        assertThat(foundUser).isNotNull();
        assertEquals("PATIENCE",foundUser.getFirstName());
        assertNull(foundUser.getCohort());
    }
    @Test
    void findUserByWrongEmailThrowsError(){
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.findUserByEmail("hello@semicolon.africa"));
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
    private UserRegistrationRequest buildSixthUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15010")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildSeventhUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15011")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildEightUser(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("chibuzo@semicolon.africa")
                .password("password")
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
    private AddAdminRequest buildChibuzo(){
        return AddAdminRequest.builder()
                .firstName("Chibuzo")
                .lastName("Ekejiuba")
                .semicolonEmail("chibuzo@semicolon.africa")
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
                .firstName("Chinedu")
                .lastName("Ugbo")
                .cohort("15")
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .semicolonID("SCV15009")
                .build();
    }
    private AddNativeRequest buildKinzy(){
        return AddNativeRequest.builder()
                .firstName("Kinzy")
                .lastName("Kinzy")
                .cohort("15")
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .semicolonID("SCV15010")
                .build();
    }
    private AddNativeRequest buildWhite(){
        return AddNativeRequest.builder()
                .firstName("Favour")
                .lastName("White")
                .cohort("15")
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .semicolonID("SCV15011")
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

    private AttendanceRequest firstAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AttendanceRequest secondAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AttendanceRequest thirdAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("chibuzo@semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private void setTimeFrame() {
        SetTimeRequest request = SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
        eliteUserService.setTimeForAttendance(request);
    }
    private SearchRequest buildSearchRequest(){
        return SearchRequest.builder()
                .startDate("23/09/2023")
                .endDate("23/09/2023")
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .cohort("15")
                .build();
    }
}