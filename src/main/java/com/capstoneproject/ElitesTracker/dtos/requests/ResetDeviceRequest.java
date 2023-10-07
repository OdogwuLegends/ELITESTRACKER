package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ResetDeviceRequest {
    private String adminSemicolonEmail;
    private String adminPassword;
    private String nativeSemicolonEmail;
    private String screenWidth;
    private String screenHeight;
}
