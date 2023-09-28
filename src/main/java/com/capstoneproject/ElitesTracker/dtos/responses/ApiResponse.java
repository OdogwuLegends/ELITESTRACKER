package com.capstoneproject.ElitesTracker.dtos.responses;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Setter
@Getter
@ToString
public class ApiResponse<T> {
    private T data;
}
