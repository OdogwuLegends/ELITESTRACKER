package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Getter;
import lombok.Setter;
@Setter
@Getter
public class PermitForAttendanceRequest {
    private String semicolonEmail;
    private String cohort;
    private boolean isAttendancePermit;
}
