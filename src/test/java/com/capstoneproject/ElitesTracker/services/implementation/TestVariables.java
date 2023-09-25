package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;

import java.time.LocalDate;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.DISABLED;
import static com.capstoneproject.ElitesTracker.enums.AttendanceStatus.PRESENT;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.localDateToString;

public class TestVariables {

    public static UserRegistrationRequest buildBobReg() {
        return UserRegistrationRequest.builder()
                .semicolonEmail("bob@native.semicolon.africa")
                .password("password")
                .scv("SCV15125")
                .screenHeight("300")
                .screenWidth("450")
                .build();
    }
    public static UserRegistrationRequest buildPatienceReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("patience@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildLegendReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .password("odogwu123")
                .scv("scv15008")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildGabrielReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("gabriel@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildSeyiReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("seyi@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildNewguyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("newPassword")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildChiboyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15009")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildCoutinhoReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("d.coutinho@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14020")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildKinzyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15010")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildWhiteReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15011")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildChibuzoReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("chibuzo@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildBlackReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15012")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildFemzReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("femi@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildJonathanReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("jonathan@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildPreciousReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("precious@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildKimReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("kimberly@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildSikiruReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("sikiru@semicolon.africa")
                .password("politician")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildJerryReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("jerry@semicolon.africa")
                .password("password")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildInemReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15013")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildNedReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15014")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildOluchiReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("o.duru@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15015")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildJideReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.farinde@native.semicolon.africa")
                .password("newPassword")
                .scv("scv15016")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildBoyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("b.boy@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14001")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildSecondBoyReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("s.boy@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14003")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static UserRegistrationRequest buildGirlReg(){
        return UserRegistrationRequest.builder()
                .semicolonEmail("g.girl@native.semicolon.africa")
                .password("newPassword")
                .scv("scv14002")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AddAdminRequest buildPatience(){
        return AddAdminRequest.builder()
                .firstName("Patience")
                .lastName("Pat")
                .semicolonEmail("patience@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildChibuzo(){
        return AddAdminRequest.builder()
                .firstName("Chibuzo")
                .lastName("Ekejiuba")
                .semicolonEmail("chibuzo@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildFemz(){
        return AddAdminRequest.builder()
                .firstName("Femi")
                .lastName("Oladeji")
                .semicolonEmail("femi@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildJonathan(){
        return AddAdminRequest.builder()
                .firstName("Jonathan")
                .lastName("Martins")
                .semicolonEmail("jonathan@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildPrecious(){
        return AddAdminRequest.builder()
                .firstName("Precious")
                .lastName("Onyeukwu")
                .semicolonEmail("precious@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildKim(){
        return AddAdminRequest.builder()
                .firstName("Kimberly")
                .lastName("Mojoyin")
                .semicolonEmail("kimberly@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildSikiru(){
        return AddAdminRequest.builder()
                .firstName("Sikiru")
                .lastName("Siks")
                .semicolonEmail("sikiru@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildJerry(){
        return AddAdminRequest.builder()
                .firstName("Jerry")
                .lastName("Chukwuma")
                .semicolonEmail("jerry@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildGabriel(){
        return AddAdminRequest.builder()
                .firstName("Gabriel")
                .lastName("Gab")
                .semicolonEmail("gabriel@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildSeyi(){
        return AddAdminRequest.builder()
                .firstName("Seyi")
                .lastName("Majek")
                .semicolonEmail("seyi@semicolon.africa")
                .build();
    }
    public static AddAdminRequest buildNewGuy(){
        return AddAdminRequest.builder()
                .firstName("NewGuy")
                .lastName("Guy")
                .semicolonEmail("newguy@semicolon.africa")
                .build();
    }
    public static AddNativeRequest buildLegend(){
        return AddNativeRequest.builder()
                .firstName("Odogwu")
                .lastName("Legend")
                .cohort("15")
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .semicolonID("SCV15008")
                .build();
    }
    public static AddNativeRequest buildChiboy(){
        return AddNativeRequest.builder()
                .firstName("Chinedu")
                .lastName("Ugbo")
                .cohort("15")
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .semicolonID("SCV15009")
                .build();
    }
    public static AddNativeRequest buildCoutinho(){
        return AddNativeRequest.builder()
                .firstName("Dominik")
                .lastName("Coutinho")
                .cohort("14")
                .semicolonEmail("d.coutinho@native.semicolon.africa")
                .semicolonID("SCV14020")
                .build();
    }
    public static AddNativeRequest buildKinzy(){
        return AddNativeRequest.builder()
                .firstName("Kinzy")
                .lastName("Kinzy")
                .cohort("15")
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .semicolonID("SCV15010")
                .build();
    }
    public static AddNativeRequest buildWhite(){
        return AddNativeRequest.builder()
                .firstName("Favour")
                .lastName("White")
                .cohort("15")
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .semicolonID("SCV15011")
                .build();
    }
    public static AddNativeRequest buildBlack(){
        return AddNativeRequest.builder()
                .firstName("Favour")
                .lastName("Black")
                .cohort("15")
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .semicolonID("SCV15012")
                .build();
    }
    public static AddNativeRequest buildInem(){
        return AddNativeRequest.builder()
                .firstName("Inemesit")
                .lastName("Udousoro")
                .cohort("15")
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .semicolonID("SCV15013")
                .build();
    }
    public static AddNativeRequest buildNed(){
        return AddNativeRequest.builder()
                .firstName("Benjamin")
                .lastName("Osisiogu")
                .cohort("15")
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .semicolonID("SCV15014")
                .build();
    }
    public static AddNativeRequest buildOluchi(){
        return AddNativeRequest.builder()
                .firstName("Oluchi")
                .lastName("Duru")
                .cohort("15")
                .semicolonEmail("o.duru@native.semicolon.africa")
                .semicolonID("SCV15015")
                .build();
    }
    public static AddNativeRequest buildJide(){
        return AddNativeRequest.builder()
                .firstName("Babajide")
                .lastName("Farinde")
                .cohort("15")
                .semicolonEmail("b.farinde@native.semicolon.africa")
                .semicolonID("SCV15016")
                .build();
    }
    public static AddNativeRequest buildFirstBoy(){
        return AddNativeRequest.builder()
                .firstName("Boy")
                .lastName("Boy")
                .cohort("14")
                .semicolonEmail("b.boy@native.semicolon.africa")
                .semicolonID("SCV14001")
                .build();
    }
    public static AddNativeRequest buildSecondBoy(){
        return AddNativeRequest.builder()
                .firstName("SecondBoy")
                .lastName("Boy")
                .cohort("14")
                .semicolonEmail("s.boy@native.semicolon.africa")
                .semicolonID("SCV14003")
                .build();
    }
    public static AddNativeRequest buildGirl(){
        return AddNativeRequest.builder()
                .firstName("Girl")
                .lastName("Girl")
                .cohort("14")
                .semicolonEmail("g.girl@native.semicolon.africa")
                .semicolonID("SCV14002")
                .build();
    }
    public static LoginRequest buildLegendLoginRequest(){
        return LoginRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .password("odogwu123")
                .build();
    }
    public static LoginRequest buildLoginRequestWithWrongEmail(){
        return LoginRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("password")
                .build();
    }
    public static LoginRequest buildLoginRequestWithWrongPassword(){
        return LoginRequest.builder()
                .semicolonEmail("newguy@semicolon.africa")
                .password("adminPassword")
                .build();
    }

    public static AttendanceRequest chiboyAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest whiteAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest kinzyAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest blackAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }

    public static AttendanceRequest chibuzoAttendanceDetails(){
        return AttendanceRequest.builder()
                .attendanceStatus(PRESENT)
                .semicolonEmail("chibuzo@semicolon.africa")
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static SetTimeRequest setTimeFrame() {
        return SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
    }
    public static SearchRequest buildKinzySearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("s.lawal@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    public static SearchRequest buildBlackSearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .cohort("15")
                .build();
    }
    public static SearchRequest buildCohort15SearchRequest(){
        return SearchRequest.builder()
                .startDate(localDateToString(LocalDate.now()))
                .endDate(localDateToString(LocalDate.now()))
                .cohort("15")
                .build();
    }
    public static SetTimeRequest buildSetTimeFrameForAttendance(){
        return SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
    }
    public static PermissionForAttendanceRequest modifyPermissionForInem(){
        return PermissionForAttendanceRequest.builder()
                .cohort("15")
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .permission(DISABLED)
                .build();
    }
    public static PermissionForAttendanceRequest modifyPermissionForCohort15(){
        return PermissionForAttendanceRequest.builder()
                .cohort("15")
                .permission(DISABLED)
                .build();
    }
    public static DeleteRequest removeCohort14(){
        return DeleteRequest.builder()
                .cohort("14")
                .build();
    }
    public static DeleteRequest removeSecondBoyInCohort14(){
        return DeleteRequest.builder()
                .semicolonEmail("s.boy@native.semicolon.africa")
                .cohort("14")
                .build();
    }
    public static ResetDeviceRequest changeNativeDevice(){
        return ResetDeviceRequest.builder()
                .adminSemicolonEmail("sikiru@semicolon.africa")
                .adminPassword("politician")
                .nativeSemicolonEmail("b.farinde@native.semicolon.africa")
                .screenWidth("1000")
                .screenHeight("1650")
                .build();
    }
    public static UpdateUserRequest updateCoutinho(){
        return UpdateUserRequest.builder()
                .semicolonEmail("d.coutinho@native.semicolon.africa")
                .cohort("15")
                .firstName("Dominic")
                .build();
    }
    public static AttendanceRequest legendAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest coutinhoAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("d.coutinho@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest chiboyAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("c.ugbo@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest blackAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("f.chiemela@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("1250")
                .screenHeight("147")
                .build();
    }
    public static AttendanceRequest inemAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest whiteAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("f.nwadike@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
    public static AttendanceRequest nedAttendance(){
        return AttendanceRequest.builder()
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .attendanceStatus(PRESENT)
                .screenWidth("550")
                .screenHeight("100")
                .build();
    }
}
