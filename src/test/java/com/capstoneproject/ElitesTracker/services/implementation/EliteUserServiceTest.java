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

import java.time.LocalDate;
import java.util.List;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.ENABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.ABSENT;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;
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
    void onlySavedUserCanRegister(){
        assertThrows(EntityDoesNotExistException.class,()-> eliteUserService.registerUser(buildUnsavedUser()));
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
        response = eliteAdminService.addNewAdmin(buildGabriel());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildGabrielReg());
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
    void nativeCanTakeAttendance(){
        response = elitesNativesService.addNewNative(buildChiboy());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChiboyReg());
        assertNotNull(userRegistrationResponse);

        setTimeFrame();

        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(chiboyAttendanceDetails(),"172.16.0.70");
        assertNotNull(attendanceResponse);
        assertEquals(attendanceMessage("CHINEDU"),attendanceResponse.getMessage());
    }
    @Test
    void adminCannotTakeAttendance(){
        response = eliteAdminService.addNewAdmin(buildChibuzo());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildChibuzoReg());
        assertNotNull(userRegistrationResponse);
        setTimeFrame();

        assertThrows(AdminsNotPermittedException.class,()-> eliteUserService.takeAttendanceTest(chibuzoAttendanceDetails(),"172.16.0.71"));
    }

    @Test
    void adminCanEditAttendanceStatusOfNative(){
        response = elitesNativesService.addNewNative(buildWhite());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildWhiteReg());
        assertNotNull(userRegistrationResponse);

        setTimeFrame();
        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(whiteAttendanceDetails(),"172.16.0.72");
        assertThat(attendanceResponse).isNotNull();

        EditAttendanceRequest request = EditAttendanceRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
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

        setTimeFrame();
        eliteUserService.takeAttendanceTest(kinzyAttendanceDetails(),"172.16.0.73");
        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForSelf(buildKinzySearchRequest());
        assertNotNull(attendanceLog);
    }
    @Test
    void adminCanSetTimeForAttendance(){
        TimeResponse response = eliteUserService.setTimeForAttendance(buildSetTimeFrameForAttendance());
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
        setTimeFrame();

        eliteUserService.takeAttendanceTest(blackAttendanceDetails(),"172.16.0.74");
        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForNative(buildBlackSearchRequest());
        assertNotNull(attendanceLog);
    }
    @Test
    void adminCanGenerateAttendanceReportForCohort(){
        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForCohort(buildCohort15SearchRequest());
        assertNotNull(attendanceLog);
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

        response = eliteAdminService.addNewAdmin(buildKim());
        assertNotNull(response);
        userRegistrationResponse = eliteUserService.registerUser(buildKimReg());
        assertNotNull(userRegistrationResponse);

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


    private static UserRegistrationRequest buildUnsavedUser() {
        return UserRegistrationRequest.builder()
                .semicolonEmail("bob@native.semicolon.africa")
                .password("password")
                .scv("SCV15125")
                .screenHeight("300")
                .screenWidth("450")
                .build();
    }
    private UserRegistrationRequest buildPatienceReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("patience@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildLegendReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .password("odogwu123")
                .scv("scv15008")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildGabrielReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("gabriel@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildNewguyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("newPassword")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildChiboyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15009")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildKinzyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15010")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildWhiteReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15011")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildChibuzoReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("chibuzo@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildBlackReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15012")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildFemzReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("femi@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildJonathanReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("jonathan@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildPreciousReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("precious@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildKimReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("kimberly@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildSikiruReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("sikiru@semicolon.africa")
                .password("politician")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildJerryReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("jerry@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildInemReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15013")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildNedReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15014")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildOluchiReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("o.duru@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15015")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildJideReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.farinde@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15016")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildBoyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.boy@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14001")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildSecondBoyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("s.boy@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14003")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private UserRegistrationRequest buildGirlReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("g.girl@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14002")
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
    private AddAdminRequest buildFemz(){
        return AddAdminRequest.builder()
                .firstName("Femi")
                .lastName("Oladeji")
                .semicolonEmail("femi@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildJonathan(){
        return AddAdminRequest.builder()
                .firstName("Jonathan")
                .lastName("Martins")
                .semicolonEmail("jonathan@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildPrecious(){
        return AddAdminRequest.builder()
                .firstName("Precious")
                .lastName("Onyeukwu")
                .semicolonEmail("precious@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildKim(){
        return AddAdminRequest.builder()
                .firstName("Kimberly")
                .lastName("Mojoyin")
                .semicolonEmail("kimberly@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildSikiru(){
        return AddAdminRequest.builder()
                .firstName("Sikiru")
                .lastName("Siks")
                .semicolonEmail("sikiru@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildJerry(){
        return AddAdminRequest.builder()
                .firstName("Jerry")
                .lastName("Chukwuma")
                .semicolonEmail("jerry@semicolon.africa")
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
    private AddNativeRequest buildBlack(){
        return AddNativeRequest.builder()
                .firstName("Favour")
                .lastName("Black")
                .cohort("15")
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .semicolonID("SCV15012")
                .build();
    }
    private AddNativeRequest buildInem(){
        return AddNativeRequest.builder()
                .firstName("Inemesit")
                .lastName("Udousoro")
                .cohort("15")
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .semicolonID("SCV15013")
                .build();
    }
    private AddNativeRequest buildNed(){
        return AddNativeRequest.builder()
                .firstName("Benjamin")
                .lastName("Osisiogu")
                .cohort("15")
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .semicolonID("SCV15014")
                .build();
    }
    private AddNativeRequest buildOluchi(){
        return AddNativeRequest.builder()
                .firstName("Oluchi")
                .lastName("Duru")
                .cohort("15")
                .semicolonEmail("o.duru@native.semicolon.africa")
                .semicolonID("SCV15015")
                .build();
    }
    private AddNativeRequest buildJide(){
        return AddNativeRequest.builder()
                .firstName("Babajide")
                .lastName("Farinde")
                .cohort("15")
                .semicolonEmail("b.farinde@native.semicolon.africa")
                .semicolonID("SCV15016")
                .build();
    }
    private AddNativeRequest buildFirstBoy(){
        return AddNativeRequest.builder()
                .firstName("Boy")
                .lastName("Boy")
                .cohort("14")
                .semicolonEmail("b.boy@native.semicolon.africa")
                .semicolonID("SCV14001")
                .build();
    }
    private AddNativeRequest buildSecondBoy(){
        return AddNativeRequest.builder()
                .firstName("SecondBoy")
                .lastName("Boy")
                .cohort("14")
                .semicolonEmail("s.boy@native.semicolon.africa")
                .semicolonID("SCV14003")
                .build();
    }
    private AddNativeRequest buildGirl(){
        return AddNativeRequest.builder()
                .firstName("Girl")
                .lastName("Girl")
                .cohort("14")
                .semicolonEmail("g.girl@native.semicolon.africa")
                .semicolonID("SCV14002")
                .build();
    }
    private LoginRequest buildLegendLoginRequest(){
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

    private AttendanceRequest chiboyAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AttendanceRequest whiteAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AttendanceRequest kinzyAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    private AttendanceRequest blackAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }

    private AttendanceRequest chibuzoAttendanceDetails(){
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
    private SearchRequest buildKinzySearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest buildBlackSearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    private SearchRequest buildCohort15SearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .cohort("15")
                .build();
    }
    private SetTimeRequest buildSetTimeFrameForAttendance(){
        return SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
    }
    private PermissionForAttendanceRequest modifyPermissionForInem(){
        return PermissionForAttendanceRequest.builder()
                .cohort("15")
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .permission(DISABLED)
                .build();
    }
    private PermissionForAttendanceRequest modifyPermissionForCohort15(){
        return PermissionForAttendanceRequest.builder()
                .cohort("15")
                .permission(DISABLED)
                .build();
    }
    private DeleteRequest removeCohort14(){
        return DeleteRequest.builder()
                .cohort("14")
                .build();
    }
    private DeleteRequest removeSecondBoyInCohort14(){
        return DeleteRequest.builder()
                .semicolonEmail("s.boy@native.semicolon.africa")
                .cohort("14")
                .build();
    }
    private ResetDeviceRequest changeNativeDevice(){
        return ResetDeviceRequest.builder()
                .adminSemicolonEmail("sikiru@semicolon.africa")
                .adminPassword("politician")
                .nativeSemicolonEmail("b.farinde@native.semicolon.africa")
                .screenWidth("1000")
                .screenHeight("1650")
                .build();
    }
}