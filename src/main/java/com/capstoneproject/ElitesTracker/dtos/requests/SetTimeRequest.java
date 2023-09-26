package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SetTimeRequest {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String adminSemicolonEmail;
}
