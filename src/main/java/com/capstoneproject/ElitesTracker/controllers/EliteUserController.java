package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.LoginRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.TestIPRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.LoginResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.models.TestIP;
import com.capstoneproject.ElitesTracker.repositories.TestIPRepository;
import com.capstoneproject.ElitesTracker.services.implementation.EliteUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.retrieveActualIP;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_USER_URL)
@CrossOrigin(origins = "*")
@Slf4j
public class EliteUserController {
    private final EliteUserService eliteUserService;
    private final TestIPRepository testIPRepository;


    @PostMapping(REGISTER)
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request){
        UserRegistrationResponse response = eliteUserService.registerUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(LOGIN_USER)
    public ResponseEntity<?> login(@RequestBody LoginRequest request){
        LoginResponse response = eliteUserService.loginUser(request);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/address")
    public String hello(TestIPRequest testIPRequest, HttpServletRequest request) {
        String address = getClientIp(request);
        log.info("IP ADDRESS IN CONTROLLER {}",address);
        TestIP testIP = TestIP.builder()
                .firstIpAddress(retrieveActualIP(request))
                .secondIpAddress(request.getRemoteAddr())
                .thirdIpAddress(address)
                .firstName(testIPRequest.getFirstName())
                .lastName(testIPRequest.getLastName())
                .build();
        testIPRepository.save(testIP);
        return "Your ip is " + address;
    }

    private String getClientIp(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // The X-Forwarded-For header can contain a comma-separated list of IP addresses,
            // where the client's IP is the leftmost entry.
            return xForwardedForHeader.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
