package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.SearchRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceSheetResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.SearchResponse;
import com.capstoneproject.ElitesTracker.models.EliteUser;

import java.util.List;

public interface SearchService {
    List<AttendanceSheetResponse> searchAttendanceReportForSelf(SearchRequest request, EliteUser foundUser);
    List<AttendanceSheetResponse> searchAttendanceReportForNative(SearchRequest request, EliteUser foundUser);
    List<AttendanceSheetResponse> searchAttendanceReportForCohort(SearchRequest request);
}
