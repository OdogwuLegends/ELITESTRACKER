package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateUserRequest {
    private String firstName;
    private String lastName;
    private String cohort;
    private String semicolonEmail;
}
