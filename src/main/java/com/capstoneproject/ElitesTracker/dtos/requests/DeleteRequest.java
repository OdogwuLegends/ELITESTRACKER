package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DeleteRequest {
    private String adminSemicolonEmail;
    private String nativeSemicolonEmail;
    private String cohort;
}
