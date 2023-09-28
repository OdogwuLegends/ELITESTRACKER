package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AddNativeRequest {
    private String firstName;
    private String lastName;
    private String semicolonEmail;
    private String cohort;
    private String semicolonID;
}
