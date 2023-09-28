package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UserRegistrationRequest {
    private String semicolonEmail;
    private String scv;
    private String password;
    private String screenWidth;
    private String screenHeight;
}
