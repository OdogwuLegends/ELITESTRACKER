package com.capstoneproject.ElitesTracker.controllers;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import com.capstoneproject.ElitesTracker.services.interfaces.AdminsService;
import com.capstoneproject.ElitesTracker.services.interfaces.NativesService;
import com.capstoneproject.ElitesTracker.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.capstoneproject.ElitesTracker.utils.ApiValues.*;

@RestController
@RequestMapping(BASE_ADMIN_URL)
@CrossOrigin(origins = "*")
@AllArgsConstructor
@Slf4j
public class AdminController {
    private final NativesService nativesService;
    private final AdminsService adminsService;
    private final UserService userService;

    @PostMapping(ADD_NATIVE)
    @Operation(description = "Endpoint to add native")
    public ResponseEntity<UserRegistrationResponse> addNative(@RequestBody @Parameter(name = "Add native request", description = "details to add native") AddNativeRequest request){
        UserRegistrationResponse response = nativesService.addNewNative(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(ADD_ADMIN)
    public ResponseEntity<UserRegistrationResponse> addAdmin(@RequestBody AddAdminRequest request){
        UserRegistrationResponse response = adminsService.addNewAdmin(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping(UPDATE_USER_PROFILE)
    public ResponseEntity<UpdateUserResponse> editUserProfile(@RequestBody UpdateUserRequest request){
        UpdateUserResponse response = userService.updateUserProfile(request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping(REMOVE_ADMIN)
    public ResponseEntity<DeleteResponse> removeAdmin(@RequestBody DeleteRequest request){
        DeleteResponse response = userService.removeAdmin(request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping(REMOVE_NATIVE)
    public ResponseEntity<DeleteResponse> removeNative(@RequestBody DeleteRequest request){
        DeleteResponse response = userService.removeNative(request);
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping(REMOVE_COHORT)
    public ResponseEntity<DeleteResponse> removeCohort(@RequestBody DeleteRequest request){
        DeleteResponse response = userService.removeCohort(request);
        return ResponseEntity.ok().body(response);
    }
    @PostMapping(SET_TIME_FRAME)
    public ResponseEntity<?> setAttendanceTime(@RequestBody SetTimeRequest request){
        TimeResponse response = userService.setTimeForAttendance(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping(EDIT_ATTENDANCE_STATUS)
    public ResponseEntity<AttendanceResponse> editNativeAttendanceStatus(@RequestBody EditAttendanceRequest request){
        AttendanceResponse response = userService.editAttendanceStatus(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping(SET_ATTENDANCE_PERMISSION_FOR_NATIVE)
    public ResponseEntity<PermissionForAttendanceResponse> setAttendancePermitForNative(@RequestBody PermissionForAttendanceRequest request){
        PermissionForAttendanceResponse response = userService.setAttendancePermissionForNative(request);
        return ResponseEntity.ok().body(response);
    }
    @PatchMapping(SET_ATTENDANCE_PERMISSION_FOR_COHORT)
    public ResponseEntity<PermissionForAttendanceResponse> setAttendancePermitForCohort(@RequestBody PermissionForAttendanceRequest request){
        PermissionForAttendanceResponse response = userService.setAttendancePermitForCohort(request);
        return ResponseEntity.ok().body(response);
    }
    @GetMapping(GENERATE_ATTENDANCE_REPORT_FOR_NATIVE)
    public ResponseEntity<List<AttendanceSheetResponse>> generateAttendanceReportForNative(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> attendanceSheet = userService.generateAttendanceReportForNative(request);
        return ResponseEntity.ok().body(attendanceSheet);
    }

    @GetMapping(GENERATE_ATTENDANCE_REPORT_FOR_COHORT)
    public ResponseEntity<List<AttendanceSheetResponse>> generateAttendanceReportForCohort(@RequestBody SearchRequest request){
        List<AttendanceSheetResponse> attendanceSheet = userService.generateAttendanceReportForCohort(request);
        return ResponseEntity.ok().body(attendanceSheet);
    }
    @GetMapping(ALL_NATIVES_IN_A_COHORT)
    public ResponseEntity<List<EliteUser>> findAllNativesInACohort(@RequestBody SearchRequest request){
        List<EliteUser> natives = userService.findAllNativesInACohort(request.getCohort());
        return ResponseEntity.ok().body(natives);
    }
    @PatchMapping(UPDATE_ADMIN_PRIVILEGE)
    public ResponseEntity<UpdateUserResponse> updateAdminPrivilege(@RequestBody EditAdminPrivilegeRequest request){
        UpdateUserResponse response = userService.updateAdminPrivilege(request);
        return ResponseEntity.ok().body(response);
    }
}
