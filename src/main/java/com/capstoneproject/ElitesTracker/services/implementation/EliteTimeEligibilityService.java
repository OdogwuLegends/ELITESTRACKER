package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.SetTimeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.TimeResponse;
import com.capstoneproject.ElitesTracker.models.TimeEligibility;
import com.capstoneproject.ElitesTracker.repositories.TimeEligibilityRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.TimeEligibilityService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.TIME_SET_MESSAGE;

@Service
@AllArgsConstructor
public class EliteTimeEligibilityService implements TimeEligibilityService {
    private TimeEligibilityRepository timeEligibilityRepository;

    @Override
    public TimeResponse setTimeForAttendance(SetTimeRequest request) {
        List<TimeEligibility> timeFrame = findAllTimeFrames();
        if(!timeFrame.isEmpty()){
            editTimeFrame(request, timeFrame);
        } else {
            buildTimeFrame(request);
        }
        return TimeResponse.builder().message(TIME_SET_MESSAGE).build();
    }

    @Override
    public List<TimeEligibility> findAllTimeFrames() {
        return timeEligibilityRepository.findAll();
    }

    private void buildTimeFrame(SetTimeRequest request) {
        TimeEligibility time = TimeEligibility.builder()
                .startHour(request.getStartHour())
                .startMinute(request.getStartMinute())
                .endHour(request.getEndHour())
                .endMinute(request.getEndMinute())
                .build();

        timeEligibilityRepository.save(time);
    }

    private void editTimeFrame(SetTimeRequest request, List<TimeEligibility> timeFrame) {

        TimeEligibility timeEligibility = timeFrame.get(0);

        timeEligibility.setStartHour(request.getStartHour());
        timeEligibility.setStartMinute(request.getStartMinute());
        timeEligibility.setEndHour(request.getEndHour());
        timeEligibility.setEndMinute(request.getEndMinute());

        timeEligibilityRepository.save(timeEligibility);
    }
}
