package com.capstoneproject.ElitesTracker.dtos.requests;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class EditAdminPrivilegeRequest {
    private String setBy;
    private String setFor;
    private String privilege;
}
