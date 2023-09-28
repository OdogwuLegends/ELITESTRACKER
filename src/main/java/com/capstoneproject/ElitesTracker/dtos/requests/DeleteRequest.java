package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class DeleteRequest {
    private String adminSemicolonEmail;
    private String nativeSemicolonEmail;
    private String cohort;
}
