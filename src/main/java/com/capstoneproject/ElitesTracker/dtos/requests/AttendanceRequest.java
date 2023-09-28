package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.*;

@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
public class AttendanceRequest {
    private String jwtToken;
    private String ipAddress;
    private String semicolonEmail;
    private String screenWidth;
    private String screenHeight;
}
