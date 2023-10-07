package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddNativeRequest {
    private String firstName;
    private String lastName;
    private String semicolonEmail;
    private String cohort;
    private String semicolonID;
}
