package com.capstoneproject.ElitesTracker.enums;

import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

public enum ExceptionMessages {
    NATIVE_DOES_NOT_EXIST_EXCEPTION(NATIVE_EXISTS),
    ATTENDANCE_ALREADY_TAKEN_EXCEPTION(ATTENDANCE_TAKEN),
    NO_ATTENDANCE_ON_WEEKENDS_EXCEPTION(WEEKDAYS_ONLY);

    ExceptionMessages(String message){
        this.message = message;
    }
    private final String message;
    public String getMessage(){
        return message;
    }
}
