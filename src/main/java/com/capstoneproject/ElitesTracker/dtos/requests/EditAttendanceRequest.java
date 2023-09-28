package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EditAttendanceRequest {
    private String nativeSemicolonEmail;
    private String cohort;
    private String date;
    private String adminSemicolonEmail;
    private AttendanceStatus attendanceStatus;
}
