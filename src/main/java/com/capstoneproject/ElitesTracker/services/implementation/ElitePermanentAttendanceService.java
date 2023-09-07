package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.models.Attendance;
import com.capstoneproject.ElitesTracker.models.PermanentAttendance;
import com.capstoneproject.ElitesTracker.repositories.PermanentAttendanceRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.AttendanceService;
import com.capstoneproject.ElitesTracker.services.interfaces.PermanentAttendanceService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ElitePermanentAttendanceService implements PermanentAttendanceService {

//    private final AttendanceService attendanceService;
    private final PermanentAttendanceRepository permanentAttendanceRepository;

    @Override
    public void savePermanentAttendance(List<Attendance> temporaryAttendances) {
//        List<Attendance> temporaryAttendances = attendanceService.findAllAttendances();

        for (Attendance eachTemporaryAttendance : temporaryAttendances) {
            PermanentAttendance permanentAttendance = new PermanentAttendance();
            BeanUtils.copyProperties(eachTemporaryAttendance,permanentAttendance);
            permanentAttendance.setUserId(eachTemporaryAttendance.getUser().getId());
            permanentAttendanceRepository.save(permanentAttendance);
        }
    }
}
