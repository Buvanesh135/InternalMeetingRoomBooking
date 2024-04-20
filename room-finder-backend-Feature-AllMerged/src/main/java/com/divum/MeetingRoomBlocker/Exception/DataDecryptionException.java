package com.divum.MeetingRoomBlocker.Exception;

public class DataDecryptionException extends RuntimeException{

    private final String message;

    public DataDecryptionException(String message){
        super(message);
        this.message=message;
    }
    public String toString(){
        return this.message;
    }
}
