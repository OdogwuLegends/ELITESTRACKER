package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request) throws EntityDoesNotExistException;
    LoginResponse loginUser(LoginRequest request);
    EliteUser findUserByEmail(String email);
    UpdateUserResponse updateUserProfile(UpdateUserRequest request);
    AttendanceResponse takeAttendance(AttendanceRequest request);
    AttendanceResponse takeAttendanceTest(AttendanceRequest request, String IpAddress);
    List<AttendanceSheetResponse> generateAttendanceReportForSelf(SearchRequest request);
    AttendanceResponse editAttendanceStatus(EditAttendanceRequest request);
    TimeResponse setTimeForAttendance(SetTimeRequest request);
    List<AttendanceSheetResponse> generateAttendanceReportForNative(SearchRequest request);
    List<AttendanceSheetResponse> generateAttendanceReportForCohort(SearchRequest request);
    List<EliteUser> findAllNativesInACohort(String cohort);
    PermissionForAttendanceResponse setAttendancePermissionForNative(PermissionForAttendanceRequest request);
    PermissionForAttendanceResponse setAttendancePermitForCohort(PermissionForAttendanceRequest request);
    DeleteResponse removeNative(DeleteRequest request);
    DeleteResponse removeAdmin(DeleteRequest request);
    DeleteResponse removeCohort(DeleteRequest request);
    ResetDeviceResponse resetNativeDevice(ResetDeviceRequest request);
}
