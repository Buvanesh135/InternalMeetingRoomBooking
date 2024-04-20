package com.divum.MeetingRoomBlocker.Exception;

public class CloudStorageException extends RuntimeException{

    private final String message;
    public CloudStorageException(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
