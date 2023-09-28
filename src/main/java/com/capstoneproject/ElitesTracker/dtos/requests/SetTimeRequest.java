package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class SetTimeRequest {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String adminSemicolonEmail;
}
