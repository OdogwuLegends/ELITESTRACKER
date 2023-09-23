package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EditAttendanceRequest {
    private String semicolonEmail;
    private String cohort;
    private String date;
    private AttendanceStatus attendanceStatus;
}
