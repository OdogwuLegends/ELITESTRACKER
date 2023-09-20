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
    AttendanceResponse takeAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest);
    List<AttendanceSheetResponse> generateAttendanceReportForSelf(SearchRequest request);
    AttendanceResponse editAttendanceStatus(EditAttendanceRequest request);
    List<AttendanceSheetResponse> generateAttendanceReportForNative(SearchRequest request);
    List<AttendanceSheetResponse> generateAttendanceReportForCohort(SearchRequest request);
    List<EliteUser> findAllNativesInACohort(String cohort);
    PermitForAttendanceResponse setAttendancePermitForNative(PermitForAttendanceRequest request);
    PermitForAttendanceResponse setAttendancePermitForCohort(PermitForAttendanceRequest request);
    DeleteResponse removeCohort(DeleteRequest request);
}
