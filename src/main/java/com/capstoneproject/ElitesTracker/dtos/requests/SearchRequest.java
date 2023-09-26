package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class SearchRequest {
    private String startDate;
    private String endDate;
    private String nativeSemicolonEmail;
    private String adminSemicolonEmail;
    private String cohort;
}
