package com.capstoneproject.ElitesTracker.services.implementation.mockito;


import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.LoginRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.LoginResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.TimeResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
import com.capstoneproject.ElitesTracker.models.*;
import com.capstoneproject.ElitesTracker.repositories.AttendanceRepository;
import com.capstoneproject.ElitesTracker.repositories.EliteUserRepository;
import com.capstoneproject.ElitesTracker.repositories.TimeEligibilityRepository;
import com.capstoneproject.ElitesTracker.services.implementation.EliteAdminService;
import com.capstoneproject.ElitesTracker.services.implementation.EliteTimeEligibilityService;
import com.capstoneproject.ElitesTracker.services.implementation.EliteUserService;
import com.capstoneproject.ElitesTracker.services.implementation.ElitesNativesService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.ENABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.enums.Role.ADMIN;
import static com.capstoneproject.ElitesTracker.enums.Role.NATIVE;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.welcomeMessage;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.LOGIN_MESSAGE;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.TIME_SET_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MockEliteUserServiceTest {
    @Mock
    private EliteUserRepository eliteUserRepository;
    @Mock
    private ElitesNativesService elitesNativesService;
    @Mock
    private EliteAdminService eliteAdminService;
    @Mock
    private TimeEligibilityRepository timeEligibilityRepository;
    @Mock
    private EliteTimeEligibilityService timeEligibilityService;
    @Mock
    private AttendanceRepository attendanceRepository;
    @Mock
    private HttpServletRequest httpServletRequest;
    @InjectMocks
    private EliteUserService eliteUserService;

    private final String PATIENCE_EMAIL = "patience@semicolon.africa";
    private final String JOHN_EMAIL = "j.doe@native.semicolon.africa";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerNativeWithRightDetails(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());

        UserRegistrationResponse response = eliteUserService.registerUser(mockRegisterNativeWithCorrectDetails());
        assertNotNull(response);
        assertEquals(welcomeMessage("JOHN"),response.getMessage());
    }
    @Test
    void registerNativeWithWrongDetailsThrowsException(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());

        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.registerUser(mockRegisterNativeWithWrongDetails()));
    }
    @Test
    void registerAdminWithCorrectDetails(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockAdmin());
        when(eliteAdminService.isExistingAdmin(PATIENCE_EMAIL)).thenReturn(true);
        when(eliteAdminService.findAdminByEmail(PATIENCE_EMAIL)).thenReturn(buildMockAddAdmin());

        UserRegistrationResponse response = eliteUserService.registerUser(mockRegisterAdminWithCorrectDetails());
        assertNotNull(response);
        assertEquals(welcomeMessage("PATIENCE"),response.getMessage());
    }
    @Test
    void registerAdminWithWrongDetails(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockAdmin());
        when(eliteAdminService.isExistingAdmin(PATIENCE_EMAIL)).thenReturn(true);
        when(eliteAdminService.findAdminByEmail(PATIENCE_EMAIL)).thenReturn(buildMockAddAdmin());

        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.registerUser(mockRegisterAdminWithWrongDetails()));
    }

    @Test
    void loginUserWithCorrectDetails(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());

        eliteUserService.registerUser(mockRegisterNativeWithCorrectDetails());
        when(eliteUserRepository.findBySemicolonEmail(any())).thenReturn(Optional.ofNullable(buildMockNative()));

        LoginResponse response = eliteUserService.loginUser(buildCorrectLoginRequest());
        assertThat(response).isNotNull();
        assertTrue(response.isLoggedIn());
        assertEquals(LOGIN_MESSAGE,response.getMessage());
    }

    @Test
    void loginUserWithWrongEmail(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());

        eliteUserService.registerUser(mockRegisterNativeWithCorrectDetails());
        when(eliteUserRepository.findBySemicolonEmail(JOHN_EMAIL)).thenReturn(Optional.ofNullable(buildMockNative()));

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongEmail()));
    }

    @Test
    void loginUserWithWrongPassword(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockAdmin());
        when(eliteAdminService.isExistingAdmin(PATIENCE_EMAIL)).thenReturn(true);
        when(eliteAdminService.findAdminByEmail(PATIENCE_EMAIL)).thenReturn(buildMockAddAdmin());

        eliteUserService.registerUser(mockRegisterAdminWithCorrectDetails());
        when(eliteUserRepository.findBySemicolonEmail(PATIENCE_EMAIL)).thenReturn(Optional.ofNullable(buildMockAdmin()));

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongPassword()));
    }

    @Test
    void findUserByCorrectEmail(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockAdmin());
        when(eliteAdminService.isExistingAdmin(PATIENCE_EMAIL)).thenReturn(true);
        when(eliteAdminService.findAdminByEmail(PATIENCE_EMAIL)).thenReturn(buildMockAddAdmin());

        eliteUserService.registerUser(mockRegisterAdminWithCorrectDetails());
        when(eliteUserRepository.findBySemicolonEmail(PATIENCE_EMAIL)).thenReturn(Optional.ofNullable(buildMockAdmin()));

        EliteUser foundUser = eliteUserService.findUserByEmail(PATIENCE_EMAIL);
        assertThat(foundUser).isNotNull();
        assertEquals("Chris",foundUser.getLastName());
    }
    @Test
    void findUserByWrongEmailThrowsError(){
        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());

        eliteUserService.registerUser(mockRegisterNativeWithCorrectDetails());
        when(eliteUserRepository.findBySemicolonEmail(JOHN_EMAIL)).thenReturn(Optional.ofNullable(buildMockNative()));

        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.findUserByEmail(PATIENCE_EMAIL));
    }
    @Test
    void setTimeFrameForAttendance(){
        when(timeEligibilityRepository.save(any())).thenReturn(buildTimeFrame());
        when(timeEligibilityRepository.findAll()).thenReturn(buildListOfTimeFrame());
        TimeResponse timeResponse = TimeResponse.builder().message(TIME_SET_MESSAGE).build();
        when(timeEligibilityService.setTimeForAttendance(any(SetTimeRequest.class))).thenReturn(timeResponse);

        TimeResponse response = timeEligibilityService.setTimeForAttendance(buildTimeFrameRequest());
        assertNotNull(response);
        response = eliteUserService.setTimeForAttendance(buildTimeFrameRequest());
        assertNotNull(response);
    }


    private EliteUser buildMockNative(){
        return EliteUser.builder()
                .firstName("John")
                .lastName("Doe")
                .semicolonEmail(JOHN_EMAIL)
                .password("password")
                .cohort("15")
                .semicolonID("SCV15001")
                .role(NATIVE)
                .permission(ENABLED)
                .screenHeight("850")
                .screenWidth("525")
                .createdAt("23/09/2023")
                .build();
    }
    private Natives buildMockAddNative(){
        return Natives.builder()
                .firstName("John")
                .lastName("Doe")
                .semicolonEmail(JOHN_EMAIL)
                .semicolonID("SCV15001")
                .cohort("15")
                .createdAt("23/09/2023")
                .build();
    }
    private UserRegistrationRequest mockRegisterNativeWithCorrectDetails(){
        return UserRegistrationRequest.builder()
                .semicolonEmail(JOHN_EMAIL)
                .password("password")
                .scv("SCV15001")
                .screenHeight("850")
                .screenWidth("525")
                .build();
    }
    private UserRegistrationRequest mockRegisterNativeWithWrongDetails(){
        return UserRegistrationRequest.builder()
                .semicolonEmail(JOHN_EMAIL)
                .password("password")
                .scv("SCV15009")
                .screenHeight("850")
                .screenWidth("525")
                .build();
    }
    private Admins buildMockAddAdmin(){
        return Admins.builder()
                .firstName("Patience")
                .lastName("Pat")
                .semicolonEmail(PATIENCE_EMAIL)
                .build();
    }
    private UserRegistrationRequest mockRegisterAdminWithCorrectDetails(){
        return UserRegistrationRequest.builder()
                .semicolonEmail(PATIENCE_EMAIL)
                .password("password")
                .screenHeight("657")
                .screenWidth("387")
                .build();
    }
    private UserRegistrationRequest mockRegisterAdminWithWrongDetails(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("hellopatience@semicolon.africa")
                .password("password")
                .screenHeight("657")
                .screenWidth("387")
                .build();
    }
    private EliteUser buildMockAdmin(){
        return EliteUser.builder()
                .firstName("Patience")
                .lastName("Chris")
                .semicolonEmail(PATIENCE_EMAIL)
                .password("password")
                .role(ADMIN)
                .screenHeight("695")
                .screenWidth("543")
                .createdAt("23/09/2023")
                .build();
    }
    private LoginRequest buildCorrectLoginRequest(){
        return LoginRequest.builder()
                .semicolonEmail(JOHN_EMAIL)
                .password("password")
                .build();
    }
    private LoginRequest buildLoginRequestWithWrongEmail(){
        return LoginRequest.builder()
                .semicolonEmail("jayjay@native.semicolon.africa")
                .password("password")
                .build();
    }
    private LoginRequest buildLoginRequestWithWrongPassword(){
        return LoginRequest.builder()
                .semicolonEmail(PATIENCE_EMAIL)
                .password("patience@123")
                .build();
    }
    private SetTimeRequest buildTimeFrameRequest(){
        return SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
    }
    private TimeEligibility buildTimeFrame() {
        return TimeEligibility.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
    }
    private List<TimeEligibility> buildListOfTimeFrame(){
       List<TimeEligibility> listOfTimeFrame = new ArrayList<>();
       listOfTimeFrame.add(buildTimeFrame());
       return listOfTimeFrame;
    }
    private Attendance buildAttendance(){
        return Attendance.builder()
                .user(buildMockNative())
                .status(PRESENT)
                .dateTaken("22/09/2023")
                .dayOfWeek("THURSDAY")
                .cohort("15")
                .ipAddress("172.16.0.70")
                .build();
    }

    private AttendanceRequest buildAttendanceRequest(){
        return AttendanceRequest.builder()
                .semicolonEmail(JOHN_EMAIL)
                .screenHeight("850")
                .screenWidth("525")
                .build();
    }
}
