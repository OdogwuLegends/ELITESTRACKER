package com.capstoneproject.ElitesTracker.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AttendanceSheetResponse {
    private String serialNumber;
    private String firstName;
    private String lastName;
    private String cohort;
    private String date;
    private String attendanceStatus;
}
