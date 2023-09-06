package com.capstoneproject.ElitesTracker.utils;

import com.capstoneproject.ElitesTracker.exceptions.NoAttendanceOnWeekendsException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.NO_ATTENDANCE_ON_WEEKENDS_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

public class App {
    public static String getCurrentTimeStamp(){
        LocalDateTime currentTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return currentTime.format(formatter);
    }
    public static String getCurrentDayOfWeek(){
        LocalDateTime currentDayOfWeek = LocalDateTime.now();
        DayOfWeek dayOfWeek = currentDayOfWeek.getDayOfWeek();
        return dayOfWeek.toString();
    }
    public static String welcomeMessage(String name) {
        return String.format(WELCOME_MESSAGE,name);
    }

    public static String saveNativeMessage(String firstName, String lastName){
        return String.format(SAVED_SUCCESSFULLY_MSG, firstName, lastName);
    }
    public static String attendanceMessage(String firstName){
        return String.format(ATTENDANCE_MESSAGE,firstName);
    }
    public static void noAttendanceOnWeekendsCheck(){
        String today = getCurrentDayOfWeek();
        if(today.equalsIgnoreCase(SATURDAY) || today.equalsIgnoreCase(SUNDAY)){
            throw new NoAttendanceOnWeekendsException(NO_ATTENDANCE_ON_WEEKENDS_EXCEPTION.getMessage());
        }
    }

    public static String retrieveActualIP(HttpServletRequest request){
        String clientIP = request.getHeader("X-Forwarded-For");

        boolean isEmpty = clientIP == null || clientIP.isEmpty() || "unknown".equalsIgnoreCase(clientIP);

        if (isEmpty) {
            clientIP = request.getHeader("Proxy-Client-IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_FORWARDED");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (isEmpty) {
            clientIP = request.getHeader("HTTP_FORWARDED");
        }
        if (isEmpty) {
            clientIP = request.getRemoteAddr();
        }
        return clientIP;
    }

}
