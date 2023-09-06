package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class UserRegistrationRequest {
    private String semicolonEmail;
    private String semicolonWifiUsername;
    private String semicolonWifiPassword;
}
