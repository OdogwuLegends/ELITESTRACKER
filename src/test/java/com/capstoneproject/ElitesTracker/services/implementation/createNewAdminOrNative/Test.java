package com.capstoneproject.ElitesTracker.services.implementation.createNewAdminOrNative;

public class Test {
    //    @Test
//    void generateAttendanceReportForSelf(){
//        SearchRequest searchRequest = SearchRequest.builder()
//                .startDate("22/09/2023")
//                .endDate("22/09/2023")
//                .semicolonEmail("c.ugbo@native.semicolon.africa")
//                .cohort("15")
//                .build();
//        List<AttendanceSheetResponse> attendanceLog = eliteUserService.generateAttendanceReportForSelf(searchRequest);
//        assertThat(attendanceLog).isNotNull();
//        assertThat(1).isEqualTo(attendanceLog.size());
//    }

    //    @Test
//    void nativeCanTakeAttendanceWithinSetTimeFrame(){
//       response = elitesNativesService.addNewNative(buildChiboy());
//       assertNotNull(response);
//       userRegistrationResponse = eliteUserService.registerUser(buildFifthUser());
//       assertNotNull(userRegistrationResponse);
//
//        setTimeFrame();
//
//        AttendanceResponse attendanceResponse = eliteUserService.takeAttendanceTest(firstAttendanceDetails(),"172.16.0.70");
//        assertNotNull(attendanceResponse);
//        assertEquals(attendanceMessage("CHINEDU"),attendanceResponse.getMessage());
//    }
//    @Test
//    void nativeCannotTakeAttendanceBeforeSetTime(){
//        response = elitesNativesService.addNewNative(buildChiboy());
//        assertNotNull(response);
//        userRegistrationResponse = eliteUserService.registerUser(buildFifthUser());
//        assertNotNull(userRegistrationResponse);
//    }


    //    @Test
//    void nativeCanTakeAttendanceWithinSetTimeFrame(){
//        when(eliteUserRepository.save(any())).thenReturn(buildMockNative());
//        when(elitesNativesService.isNative(JOHN_EMAIL,"SCV15001")).thenReturn(true);
//        when(elitesNativesService.findNativeByEmail(any())).thenReturn(buildMockAddNative());
//        eliteUserService.registerUser(mockRegisterNativeWithCorrectDetails());
//
//        SetTimeRequest request = SetTimeRequest.builder()
//                .startHour(1)
//                .startMinute(0)
//                .endHour(23)
//                .endMinute(59)
//                .build();
//        eliteUserService.setTimeForAttendance(request);
//
//        when(attendanceRepository.save(any())).thenReturn(Optional.ofNullable(buildAttendance()));
//        when(attendanceRepository.findByIpAddress(any())).thenReturn(Optional.ofNullable(buildAttendance()));
//
//        AttendanceResponse response = eliteUserService.takeAttendance(buildAttendanceRequest(),httpServletRequest);
//        assertNotNull(response);
//    }


}
