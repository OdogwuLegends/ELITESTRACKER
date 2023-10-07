package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SetTimeRequest {
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;
    private String adminSemicolonEmail;
}
