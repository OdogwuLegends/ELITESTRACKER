package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.TimeResponse;
import com.capstoneproject.ElitesTracker.models.TimeEligibility;

import java.util.List;

public interface TimeEligibilityService {
    TimeResponse setTimeForAttendance(SetTimeRequest request);
    List<TimeEligibility> findAllTimeFrames();

}
