package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditAttendanceRequest {
    private String semicolonEmail;
    private String cohort;
    private String date;
    private AttendanceStatus attendanceStatus;
}
