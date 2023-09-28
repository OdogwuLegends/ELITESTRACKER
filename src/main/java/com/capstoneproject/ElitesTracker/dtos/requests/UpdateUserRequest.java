package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String cohort;
    private String semicolonEmail;
    private String updatedSemicolonEmail;
    private String adminSemicolonEmail;
    private String semicolonID;
}
