package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddAdminRequest {
    private String firstName;
    private String lastName;
    private String semicolonEmail;

}
