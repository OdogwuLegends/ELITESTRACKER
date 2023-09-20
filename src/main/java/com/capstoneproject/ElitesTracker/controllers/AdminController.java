package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.AddAdminRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.AddNativeRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.EditAttendanceRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.SearchRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.AttendanceSheetResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.services.interfaces.AdminsService;
import com.capstoneproject.ElitesTracker.services.interfaces.NativesService;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@RequestMapping(BASE_SUPER_ADMIN_URL)
@CrossOrigin("*")
@AllArgsConstructor
public class AdminController {
    private final NativesService nativesService;
    private final AdminsService adminsService;
    private final UserService userService;

    @PostMapping(ADD_NATIVE)
    public ResponseEntity<UserRegistrationResponse> addNative(@RequestBody AddNativeRequest request){
        UserRegistrationResponse response = nativesService.addNewNative(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(ADD_ADMIN)
    public ResponseEntity<UserRegistrationResponse> addAdmin(@RequestBody AddAdminRequest request){
        UserRegistrationResponse response = adminsService.addNewAdmin(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/editAttendanceStatus")
    public ResponseEntity<AttendanceResponse> editNativeAttendanceStatus(@RequestBody EditAttendanceRequest request){
        AttendanceResponse response = userService.editAttendanceStatus(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping("/generateAttendanceForNative")
    public ResponseEntity<List<AttendanceSheetResponse>> generateAttendanceReportForNative(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> attendanceSheet = userService.generateAttendanceReportForNative(request);
        return ResponseEntity.ok().body(attendanceSheet);
    }

    @GetMapping("/generateAttendanceForCohort")
    public ResponseEntity<List<AttendanceSheetResponse>> generateAttendanceReportForCohort(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> attendanceSheet = userService.generateAttendanceReportForCohort(request);
        return ResponseEntity.ok().body(attendanceSheet);
    }
}
