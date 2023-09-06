package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.NativeDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import com.capstoneproject.ElitesTracker.models.MockSemicolonDB;
import com.capstoneproject.ElitesTracker.repositories.EliteUserRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.AttendanceService;
import com.capstoneproject.ElitesTracker.services.interfaces.SemicolonDbService;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.Role.NATIVE;
import static com.capstoneproject.ElitesTracker.utils.App.retrieveActualIP;
import static com.capstoneproject.ElitesTracker.utils.App.welcomeMessage;

@Service
@AllArgsConstructor
@Slf4j
public class EliteUserService implements UserService {
    private final EliteUserRepository eliteUserRepository;
    private final SemicolonDbService semicolonDbService;
    private final AttendanceService attendanceService;


    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request, HttpServletRequest httpServletRequest) throws NativeDoesNotExistException {
        MockSemicolonDB existingNative = getExistingNative(request);
        EliteUser eliteUser = buildEliteUser(request, existingNative, httpServletRequest);
        EliteUser savedUser = eliteUserRepository.save(eliteUser);
        return UserRegistrationResponse.builder()
                .message(welcomeMessage(savedUser.getFirstName().toUpperCase()))
                .build();
    }

    @Override
    public AttendanceResponse takeAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest) {
        Optional<EliteUser> foundNative = eliteUserRepository.findBySemicolonEmail(request.getSemicolonEmail());
        return attendanceService.saveAttendance(request,httpServletRequest,foundNative.get());
    }

    private EliteUser buildEliteUser(UserRegistrationRequest request, MockSemicolonDB existingNative, HttpServletRequest httpServletRequest) {
        return EliteUser.builder()
                .firstName(existingNative.getFirstName())
                .lastName(existingNative.getLastName())
                .cohort(existingNative.getCohort())
                .semicolonEmail(existingNative.getSemicolonEmail())
                .semicolonWifiPassword(request.getSemicolonWifiPassword()) //Encode password after security added
                .semicolonWifiUsername(request.getSemicolonWifiUsername())
                .role(NATIVE)
                .IpAddress(retrieveActualIP(httpServletRequest))
//                .IpAddress(httpServletRequest.getRemoteAddr())
                .build();

    }
    private MockSemicolonDB getExistingNative(UserRegistrationRequest request) throws NativeDoesNotExistException {
        return semicolonDbService.findNative(request.getSemicolonEmail());
    }



}
