package com.capstoneproject.ElitesTracker.exceptions;

public class AdminsNotPermittedException extends RuntimeException {
    public AdminsNotPermittedException(String message){
        super(message);
    }
}
