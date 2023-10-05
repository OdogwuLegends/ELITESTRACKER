package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.LoginRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.ResetPasswordRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.UserRegistrationRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.LoginResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.ResetPasswordResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.services.implementation.EliteUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@AllArgsConstructor
@RequestMapping(BASE_USER_URL)
@CrossOrigin(origins = "*")
@Slf4j
public class EliteUserController {
    private final EliteUserService eliteUserService;


    @PostMapping(REGISTER)
    public ResponseEntity<UserRegistrationResponse> register(@RequestBody UserRegistrationRequest request){
        UserRegistrationResponse response = eliteUserService.registerUser(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(LOGIN_USER)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        LoginResponse response = eliteUserService.loginUser(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping(EMAIL_FOR_PASSWORD_RESET)
    public ResponseEntity<ResetPasswordResponse> sendEmailForPasswordReset(@RequestBody ResetPasswordRequest request){
        ResetPasswordResponse response = eliteUserService.sendEmailForPasswordReset(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping(RESET_PASSWORD)
    public ResponseEntity<ResetPasswordResponse> resetPassword(@RequestBody ResetPasswordRequest request){
        ResetPasswordResponse response = eliteUserService.resetPassword(request);
        return ResponseEntity.ok().body(response);
    }
}
