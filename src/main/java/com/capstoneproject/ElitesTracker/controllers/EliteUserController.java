package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.models.TestIP;
import com.capstoneproject.ElitesTracker.repositories.TestIPRepository;
import com.capstoneproject.ElitesTracker.services.implementation.EliteUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.capstoneproject.ElitesTracker.utils.App.retrieveActualIP;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class EliteUserController {
    private final EliteUserService eliteUserService;
    private final TestIPRepository testIPRepository;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request, HttpServletRequest httpServletRequest){
        UserRegistrationResponse response = eliteUserService.registerUser(request, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/takeAttendance")
    public ResponseEntity<AttendanceResponse> takeAttendance(@RequestBody AttendanceRequest request, HttpServletRequest httpServletRequest){
        AttendanceResponse response = eliteUserService.takeAttendance(request, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/address")
    public String hello(HttpServletRequest request) {
        log.info("IP ADDRESS IN CONTROLLER {}",request.getRemoteAddr());
        TestIP testIP = TestIP.builder()
                .IpAddress(retrieveActualIP(request))
                .build();
        testIPRepository.save(testIP);
        return "Your ip is " + request.getRemoteAddr();
    }
}
