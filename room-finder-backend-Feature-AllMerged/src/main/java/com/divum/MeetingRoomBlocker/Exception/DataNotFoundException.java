package com.divum.MeetingRoomBlocker.Exception;

public class DataNotFoundException extends RuntimeException{
    private final String message;
    public DataNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    public String toString(){
        return this.message;
    }
}
