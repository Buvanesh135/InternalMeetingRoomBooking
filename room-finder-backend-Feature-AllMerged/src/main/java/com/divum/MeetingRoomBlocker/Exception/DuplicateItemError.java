package com.divum.MeetingRoomBlocker.Exception;

public class DuplicateItemError extends RuntimeException{

    private final String message;
    public DuplicateItemError(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
