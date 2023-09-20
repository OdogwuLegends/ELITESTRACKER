package com.capstoneproject.ElitesTracker.dtos.responses;

import com.capstoneproject.ElitesTracker.models.Attendance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class SearchResponse {
    private List<Attendance> attendanceLog;
    private String message;
}
