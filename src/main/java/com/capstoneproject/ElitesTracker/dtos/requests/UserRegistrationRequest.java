package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {
    private String semicolonEmail;
    private String scv;
    private String password;
    private String screenWidth;
    private String screenHeight;
}
