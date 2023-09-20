package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {
    private String semicolonEmail;
    private String cohort;
}
