package com.divum.MeetingRoomBlocker.Exception;

public class InvalidTokenException extends RuntimeException{
    private final String message;

    public InvalidTokenException(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
