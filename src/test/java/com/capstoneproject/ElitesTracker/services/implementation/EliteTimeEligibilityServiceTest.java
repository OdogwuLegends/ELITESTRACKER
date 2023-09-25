package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.TimeResponse;
import com.capstoneproject.ElitesTracker.models.TimeEligibility;
import com.capstoneproject.ElitesTracker.services.interfaces.TimeEligibilityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.TIME_SET_MESSAGE;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("dev")
class EliteTimeEligibilityServiceTest {
    @Autowired
    private TimeEligibilityService timeEligibilityService;

    @Test
    void adminCanSetTimeForAttendance(){
        SetTimeRequest request = SetTimeRequest.builder()
                .startHour(1)
                .startMinute(0)
                .endHour(23)
                .endMinute(59)
                .build();
        TimeResponse response = timeEligibilityService.setTimeForAttendance(request);
        assertThat(response).isNotNull();
        assertEquals(TIME_SET_MESSAGE,response.getMessage());
    }
    @Test
    void adminCanFindAllTimeFrames(){
        List<TimeEligibility> timeFrames = timeEligibilityService.findAllTimeFrames();
        assertNotNull(timeFrames);
        assertEquals(1,timeFrames.size());
    }
    @Test
    void onlyOneObjectOfSetTimeCanExistInDatabase(){
        SetTimeRequest firstRequest = SetTimeRequest.builder()
                .startHour(2)
                .startMinute(0)
                .endHour(22)
                .endMinute(5)
                .build();
        TimeResponse response = timeEligibilityService.setTimeForAttendance(firstRequest);
        assertThat(response).isNotNull();
        assertEquals(TIME_SET_MESSAGE,response.getMessage());

        SetTimeRequest secondRequest = SetTimeRequest.builder()
                .startHour(2)
                .startMinute(0)
                .endHour(22)
                .endMinute(5)
                .build();
        TimeResponse timeResponse = timeEligibilityService.setTimeForAttendance(secondRequest);
        assertThat(timeResponse).isNotNull();
        assertEquals(TIME_SET_MESSAGE,timeResponse.getMessage());

        List<TimeEligibility> timeFrames = timeEligibilityService.findAllTimeFrames();
        assertNotNull(timeFrames);
        assertEquals(1,timeFrames.size());
    }
}