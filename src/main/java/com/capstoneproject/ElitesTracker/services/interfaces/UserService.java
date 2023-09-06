package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.NativeDoesNotExistException;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request, HttpServletRequest httpServletRequest) throws NativeDoesNotExistException;
    AttendanceResponse takeAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest);
}
