package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
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
