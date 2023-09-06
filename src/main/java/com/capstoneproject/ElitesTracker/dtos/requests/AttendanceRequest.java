package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AttendanceRequest {
    private AttendanceStatus attendanceStatus;
    private String semicolonEmail;
}
