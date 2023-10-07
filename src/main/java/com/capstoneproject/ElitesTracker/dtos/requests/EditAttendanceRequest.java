package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendanceStatus;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EditAttendanceRequest {
    private String nativeSemicolonEmail;
    private String cohort;
    private String date;
    private String adminSemicolonEmail;
    private AttendanceStatus attendanceStatus;
}
