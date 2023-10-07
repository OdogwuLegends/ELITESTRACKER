package com.capstoneproject.ElitesTracker.dtos.requests;

import com.capstoneproject.ElitesTracker.enums.AttendancePermission;
import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PermissionForAttendanceRequest {
    private String nativeSemicolonEmail;
    private String cohort;
    private AttendancePermission permission;
    private String adminSemicolonEmail;
}
