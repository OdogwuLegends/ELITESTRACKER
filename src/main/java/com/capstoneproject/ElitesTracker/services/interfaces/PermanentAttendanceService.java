package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.models.Attendance;

import java.util.List;

public interface PermanentAttendanceService {
    void savePermanentAttendance(List<Attendance> temporaryAttendances);
}
