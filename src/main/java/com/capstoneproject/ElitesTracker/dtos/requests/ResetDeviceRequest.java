package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetDeviceRequest {
    private String adminSemicolonEmail;
    private String adminPassword;
    private String nativeSemicolonEmail;
    private String screenWidth;
    private String screenHeight;
}
