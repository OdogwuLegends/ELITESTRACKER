package com.capstoneproject.ElitesTracker.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ResetPasswordResponse {
    private String message;
}
