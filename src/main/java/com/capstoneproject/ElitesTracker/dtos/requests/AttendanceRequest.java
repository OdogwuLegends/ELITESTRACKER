package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AttendanceRequest {
    private String attendanceStatus;
    private String semicolonEmail;
    private String screenWidth;
    private String screenHeight;
}
