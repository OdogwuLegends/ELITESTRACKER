package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.MockDbRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.services.interfaces.SemicolonDbService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/native")
@AllArgsConstructor
public class MockSemicolonController {
    private final SemicolonDbService semicolonDbService;

    @PostMapping("/register")
    public ResponseEntity<?> registerNative(@RequestBody MockDbRequest request){
        UserRegistrationResponse response = semicolonDbService.registerNative(request);
        return ResponseEntity.ok().body(response);
    }
}
