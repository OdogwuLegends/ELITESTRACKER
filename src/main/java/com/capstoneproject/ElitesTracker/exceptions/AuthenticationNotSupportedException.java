package com.capstoneproject.ElitesTracker.exceptions;

public class AuthenticationNotSupportedException extends RuntimeException{
    public AuthenticationNotSupportedException(String message){
        super(message);
    }
}
