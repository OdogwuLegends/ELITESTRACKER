package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.ResetDeviceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SearchRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceSheetResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.ResetDeviceResponse;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@RequestMapping(NATIVES_BASE_URL)
@AllArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class NativesController {
    private final UserService userService;

    @PostMapping(TAKE_ATTENDANCE)
    public ResponseEntity<AttendanceResponse> takeAttendance(@RequestBody AttendanceRequest request){
        AttendanceResponse response = userService.takeAttendance(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(GENERATE_REPORT_FOR_SELF)
    public ResponseEntity<List<AttendanceSheetResponse>> generateAttendanceReportForSelf(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> responses = userService.generateAttendanceReportForSelf(request);
        return ResponseEntity.ok().body(responses);
    }
    @PatchMapping(RESET_DEVICE)
    public ResponseEntity<ResetDeviceResponse> resetDevice(@RequestBody ResetDeviceRequest request){
        ResetDeviceResponse response = userService.resetNativeDevice(request);
        return ResponseEntity.ok().body(response);
    }
}
