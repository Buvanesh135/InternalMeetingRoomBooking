package com.divum.MeetingRoomBlocker.Exception;

public class LossOfDataException extends RuntimeException{
    private final String message;
    public LossOfDataException(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
