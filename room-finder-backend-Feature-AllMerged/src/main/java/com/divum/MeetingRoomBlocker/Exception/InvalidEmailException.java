package com.divum.MeetingRoomBlocker.Exception;


public class InvalidEmailException extends RuntimeException{

    private final String message;

    public InvalidEmailException(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
