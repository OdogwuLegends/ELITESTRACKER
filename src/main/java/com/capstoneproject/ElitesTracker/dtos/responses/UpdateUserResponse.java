package com.capstoneproject.ElitesTracker.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class UpdateUserResponse {
    private String message;
}
