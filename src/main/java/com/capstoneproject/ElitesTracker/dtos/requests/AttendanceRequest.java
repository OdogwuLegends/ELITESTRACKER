package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequest {
    private String jwtToken;
    private String ipAddress;
    private String ipAddressConcat;
    private String semicolonEmail;
    private String screenWidth;
    private String screenHeight;
    private String attendanceDate;
    private String fiftyOneDegrees;
}
