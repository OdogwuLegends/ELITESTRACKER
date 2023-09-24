package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendancePermission;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
@Builder
public class PermissionForAttendanceRequest {
    private String semicolonEmail;
    private String cohort;
    private AttendancePermission permission;
}