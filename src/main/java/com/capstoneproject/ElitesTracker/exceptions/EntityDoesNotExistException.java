package com.capstoneproject.ElitesTracker.exceptions;

public class EntityDoesNotExistException extends RuntimeException {
    public EntityDoesNotExistException(String message) {
        super(message);
    }
}
