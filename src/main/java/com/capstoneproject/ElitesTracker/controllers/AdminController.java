package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.services.interfaces.AdminsService;
import com.capstoneproject.ElitesTracker.services.interfaces.NativesService;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@RequestMapping(BASE_SUPER_ADMIN_URL)
@CrossOrigin("*")
@AllArgsConstructor
@Slf4j
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
    @DeleteMapping("/removeAdmin")
    public ResponseEntity<DeleteResponse> removeAdmin(@RequestBody DeleteRequest request){
        DeleteResponse response = adminsService.removeAdmin(request);
        return ResponseEntity.ok().body(response);
    }
//    @DeleteMapping("/removeCohort")
//    public ResponseEntity<DeleteResponse> removeCohort(@RequestBody DeleteRequest request){
//        DeleteResponse response = userService.removeCohort(request);
//        return ResponseEntity.ok().body(response);
//    }
    @PostMapping("/setTimeFrame")
    public ResponseEntity<?> setAttendanceTime(@RequestBody SetTimeRequest request){
        TimeResponse response = userService.setTimeForAttendance(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/editAttendanceStatus")
    public ResponseEntity<AttendanceResponse> editNativeAttendanceStatus(@RequestBody EditAttendanceRequest request){
        AttendanceResponse response = userService.editAttendanceStatus(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/setAttendancePermitForNative")
    public ResponseEntity<PermitForAttendanceResponse> setAttendancePermitForNative(@RequestBody PermitForAttendanceRequest request){
        PermitForAttendanceResponse response = userService.setAttendancePermitForNative(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping("/setAttendancePermitForCohort")
    public ResponseEntity<PermitForAttendanceResponse> setAttendancePermitForCohort(@RequestBody PermitForAttendanceRequest request){
        PermitForAttendanceResponse response = userService.setAttendancePermitForCohort(request);
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
