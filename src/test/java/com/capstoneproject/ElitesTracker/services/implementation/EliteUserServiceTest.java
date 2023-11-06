package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.exceptions.AdminsNotPermittedException;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
import com.capstoneproject.ElitesTracker.exceptions.UserExistsException;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.ENABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.ABSENT;
import static com.capstoneproject.ElitesTracker.services.implementation.TestVariables.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
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
    void onlySavedUserCanRegister(){
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.registerUser(buildBobReg()));
    }

    @Test
    void registerUser() {
        response = eliteAdminService.addNewAdmin(buildPatience());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildPatienceReg());
        assertNotNull(userRegistrationResponse);
        assertEquals(welcomeMessage("PATIENCE"),userRegistrationResponse.getMessage());
    }
    @Test
    void registerUserThrowsErrorIfUserExists(){
        assertThrows(UserExistsException.class,()-> eliteUserService.registerUser(buildPatienceReg()));
    }
    @Test
    void loginUserWithCorrectionDetails() {
        response = elitesNativesService.addNewNative(buildLegend());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildLegendReg());
        assertNotNull(userRegistrationResponse);

        LoginResponse loginResponse = eliteUserService.loginUser(buildLegendLoginRequest());
        assertNotNull(loginResponse);
        assertTrue(loginResponse.isLoggedIn());
        assertEquals(LOGIN_MESSAGE, loginResponse.getMessage());
    }
    @Test
    void loginUserWithWrongEmailThrowsException(){
        response = eliteAdminService.addNewAdmin(buildGrace());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildGraceReg());
        assertNotNull(userRegistrationResponse);

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongEmail()));
    }
    @Test
    void loginUserWithWrongPasswordThrowsException(){
        response = eliteAdminService.addNewAdmin(buildNewGuy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildNewguyReg());
        assertNotNull(userRegistrationResponse);

        assertThrows(IncorrectDetailsException.class,()-> eliteUserService.loginUser(buildLoginRequestWithWrongPassword()));
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
    @Test
    void adminCanUpdateNativeProfile(){
        response = elitesNativesService.addNewNative(buildCoutinho());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildCoutinhoReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildSeyi());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildSeyiReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("seyi@semicolon.africa")
                .privilege("SUB")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        EliteUser foundUser = eliteUserService.findUserByEmail("d.coutinho@native.semicolon.africa");
        assertNotNull(foundUser);
        assertEquals("DOMINIK",foundUser.getFirstName());
        assertEquals("14",foundUser.getCohort());

        updateUserResponse = eliteUserService.updateUserProfile(updateCoutinho());
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        foundUser = eliteUserService.findUserByEmail("d.coutinho@native.semicolon.africa");
        assertNotNull(foundUser);
        assertEquals("DOMINIC",foundUser.getFirstName());
        assertEquals("15",foundUser.getCohort());
        assertEquals("d.coutinho@native.semicolon.africa",foundUser.getSemicolonEmail());
    }
    @Test
    void nativeCanTakeAttendance(){
        response = elitesNativesService.addNewNative(buildChiboy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChiboyReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendanceForTest(request);

        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(chiboyAttendanceDetails(),"172.16.0.70");
        assertNotNull(attendanceResponse);
        assertEquals(normalAttendanceMessage("CHINEDU"),attendanceResponse.getMessage());
    }
    @Test
    void adminCannotTakeAttendance(){
        response = eliteAdminService.addNewAdmin(buildChibuzo());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChibuzoReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest request = setTimeFrame();
        eliteUserService.setTimeForAttendanceForTest(request);

        assertThrows(AdminsNotPermittedException.class,()-> eliteUserService.takeAttendanceTest(chibuzoAttendanceDetails(),"172.16.0.71"));
    }

    @Test
    void adminCanEditAttendanceStatusOfNative(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildWhiteReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildGabriel());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildGabrielReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("gabriel@semicolon.africa")
                .privilege("SUB")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());


        SetTimeRequest setTimeRequest = setTimeFrame();
        eliteUserService.setTimeForAttendanceForTest(setTimeRequest);

        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(whiteAttendanceDetails(),"172.16.0.72");
        assertThat(attendanceResponse).isNotNull();

        EditAttendanceRequest request = EditAttendanceRequest.builder()
                .nativeSemicolonEmail("f.nwadike@native.semicolon.africa")
                .adminSemicolonEmail("gabriel@semicolon.africa")
                .cohort("15")
                .date(localDateToString(LocalDate.now()))
                .attendanceStatus(ABSENT)
                .build();
        attendanceResponse = eliteUserService.editAttendanceStatus(request);
        assertThat(attendanceResponse).isNotNull();
        assertEquals(EDIT_STATUS_MESSAGE,attendanceResponse.getMessage());
    }

   @Test
    void nativeCanGenerateAttendanceForSelf(){
        response = elitesNativesService.addNewNative(buildKinzy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildKinzyReg());
        assertNotNull(userRegistrationResponse);

       SetTimeRequest setTimeRequest = setTimeFrame();
       eliteUserService.setTimeForAttendanceForTest(setTimeRequest);

        eliteUserService.takeAttendanceTest(kinzyAttendanceDetails(),"172.16.0.73");
        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForSelf(buildKinzySearchRequest());
        assertNotNull(attendanceLog);
    }
    @Test
    void adminCanSetTimeForAttendance(){
        TimeResponse response = eliteUserService.setTimeForAttendanceForTest(buildSetTimeFrameForAttendance());
        assertNotNull(response);
        assertEquals(response.getMessage(),TIME_SET_MESSAGE);
    }
    @Test
    void adminCanGenerateAttendanceReportForNative(){
        response = elitesNativesService.addNewNative(buildBlack());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildBlackReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildFemz());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildFemzReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest setTimeRequest = setTimeFrame();
        eliteUserService.setTimeForAttendanceForTest(setTimeRequest);

        eliteUserService.takeAttendanceTest(blackAttendanceDetails(),"172.16.0.74");
        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForNative(buildBlackSearchRequest());
        assertNotNull(attendanceLog);
    }
    @Test
    void adminCanGenerateAttendanceReportForCohort(){
        response = elitesNativesService.addNewNative(buildBolaji());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildBolajiReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildKim());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildKimReg());
        assertNotNull(userRegistrationResponse);

        SetTimeRequest setTimeRequest = setTimeFrame();
        eliteUserService.setTimeForAttendanceForTest(setTimeRequest);

        eliteUserService.takeAttendanceTest(bolajiAttendanceDetails(),"172.16.0.75");

        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForCohort(buildCohort15SearchRequest());
        assertNotNull(attendanceLog);
        assertThat(attendanceLog.size()).isBetween(1,10);
    }
    @Test
    void adminCanSetAttendancePermissionForNative(){
        response = elitesNativesService.addNewNative(buildInem());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildInemReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildJonathan());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildJonathanReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("jonathan@semicolon.africa")
                .privilege("SUB")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        EliteUser foundUser = eliteUserService.findUserByEmail("i.udousoro@native.semicolon.africa");
        assertEquals(ENABLED,foundUser.getPermission());

        PermissionForAttendanceResponse attendanceResponse = eliteUserService.setAttendancePermissionForNative(modifyPermissionForInem());
        assertThat(attendanceResponse).isNotNull();
        assertEquals(PERMISSION_MODIFIED_MESSAGE,attendanceResponse.getMessage());

        foundUser = eliteUserService.findUserByEmail("i.udousoro@native.semicolon.africa");
        assertEquals(DISABLED,foundUser.getPermission());
    }

    @Test
    void adminCanSetAttendancePermissionForCohort(){
        response = elitesNativesService.addNewNative(buildNed());
        assertNotNull(response);
        response = elitesNativesService.addNewNative(buildOluchi());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildNedReg());
        assertNotNull(userRegistrationResponse);
        userRegistrationResponse = eliteUserService.registerUser(buildOluchiReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildPrecious());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildPreciousReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("precious@semicolon.africa")
                .privilege("SUB")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        EliteUser foundUser = eliteUserService.findUserByEmail("b.osisiogu@native.semicolon.africa");
        assertEquals(ENABLED,foundUser.getPermission());
        foundUser = eliteUserService.findUserByEmail("o.duru@native.semicolon.africa");
        assertEquals(ENABLED,foundUser.getPermission());


        PermissionForAttendanceResponse attendanceResponse = eliteUserService.setAttendancePermitForCohort(modifyPermissionForCohort15());
        assertThat(attendanceResponse).isNotNull();
        assertEquals(PERMISSION_MODIFIED_MESSAGE,attendanceResponse.getMessage());

        foundUser = eliteUserService.findUserByEmail("b.osisiogu@native.semicolon.africa");
        assertEquals(DISABLED,foundUser.getPermission());
        foundUser = eliteUserService.findUserByEmail("o.duru@native.semicolon.africa");
        assertEquals(DISABLED,foundUser.getPermission());
    }
    @Test
    void adminCanFindAllUsers(){
        List<EliteUser> foundUsers = eliteUserService.findAllNativesInACohort("15");
        assertNotNull(foundUsers);
        for (int i = 0; i < foundUsers.size(); i++) {
            System.err.println((i+1)+".) "+foundUsers.get(i).getFirstName() + " " + foundUsers.get(i).getLastName());
        }
    }
    @Test
    void adminCanDeleteANative(){
        response = elitesNativesService.addNewNative(buildSecondBoy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildSecondBoyReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildTimothy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildTimothyReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("timothy@semicolon.africa")
                .privilege("SUPER")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        DeleteResponse deleteResponse = eliteUserService.removeNative(removeSecondBoyInCohort14());
        assertThat(deleteResponse).isNotNull();
        assertEquals(DELETE_USER_MESSAGE,deleteResponse.getMessage());
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.findUserByEmail("s.boy@native.semicolon.africa"));
    }
    @Test
    void adminCanDeleteCohort(){
        response = elitesNativesService.addNewNative(buildFirstBoy());
        assertNotNull(response);
        response = elitesNativesService.addNewNative(buildGirl());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildBoyReg());
        assertNotNull(userRegistrationResponse);
        userRegistrationResponse = eliteUserService.registerUser(buildGirlReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildJerry());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildJerryReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("jerry@semicolon.africa")
                .privilege("SUPER")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        DeleteResponse deleteResponse = eliteUserService.removeCohort(removeCohort14());
        assertNotNull(deleteResponse);
        assertEquals(DELETE_USER_MESSAGE,deleteResponse.getMessage());
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.findUserByEmail("b.boy@native.semicolon.africa"));
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.findUserByEmail("g.girl@native.semicolon.africa"));
    }
    @Test
    void nativeCanChangeDeviceWithAdminConsent(){
        response = elitesNativesService.addNewNative(buildJide());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildJideReg());
        assertNotNull(userRegistrationResponse);

        response = eliteAdminService.addNewAdmin(buildSikiru());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildSikiruReg());
        assertNotNull(userRegistrationResponse);

        EditAdminPrivilegeRequest editAdminPrivilegeRequest = EditAdminPrivilegeRequest.builder()
                .setFor("sikiru@semicolon.africa")
                .privilege("SUB")
                .build();

        UpdateUserResponse updateUserResponse = eliteUserService.updateAdminPrivilegeForTest(editAdminPrivilegeRequest);
        assertNotNull(updateUserResponse);
        assertEquals(PROFILE_UPDATE_SUCCESSFUL,updateUserResponse.getMessage());

        EliteUser foundUser = eliteUserService.findUserByEmail("b.farinde@native.semicolon.africa");
        assertNotNull(foundUser);
        assertEquals("550",foundUser.getScreenWidth());
        assertEquals("100",foundUser.getScreenHeight());

        ResetDeviceResponse resetDeviceResponse = eliteUserService.resetNativeDevice(changeNativeDevice());
        assertNotNull(resetDeviceResponse);
        assertEquals(DEVICE_RESET_MESSAGE,resetDeviceResponse.getMessage());

        foundUser = eliteUserService.findUserByEmail("b.farinde@native.semicolon.africa");
        assertNotNull(foundUser);
        assertEquals("1000",foundUser.getScreenWidth());
        assertEquals("1650",foundUser.getScreenHeight());
    }



}