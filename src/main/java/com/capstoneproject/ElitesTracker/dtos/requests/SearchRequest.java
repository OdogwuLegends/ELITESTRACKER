package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private String startDate;
    private String endDate;
    private String nativeSemicolonEmail;
    private String adminSemicolonEmail;
    private String cohort;
}
