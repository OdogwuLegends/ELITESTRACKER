package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class ResetPasswordRequest {
    private String semicolonEmail;
    private String newPassword;
    private String token;
}
