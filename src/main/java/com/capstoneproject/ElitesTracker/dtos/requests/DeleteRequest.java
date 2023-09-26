package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DeleteRequest {
    private String adminSemicolonEmail;
    private String nativeSemicolonEmail;
    private String cohort;
}
