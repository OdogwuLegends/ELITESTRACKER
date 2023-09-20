package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.AttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SearchRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceSheetResponse;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@RequestMapping(NATIVES_BASE_URL)
@AllArgsConstructor
public class NativesController {
    private final UserService userService;

    @PostMapping(TAKE_ATTENDANCE)
    public ResponseEntity<AttendanceResponse> takeAttendance(@RequestBody AttendanceRequest request, HttpServletRequest httpServletRequest){
        AttendanceResponse response = userService.takeAttendance(request, httpServletRequest);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(GENERATE_REPORT_FOR_SELF)
    public ResponseEntity<?> generateAttendanceReportForSelf(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> responses = userService.generateAttendanceReportForSelf(request);
        return ResponseEntity.ok().body(responses);
    }
}
