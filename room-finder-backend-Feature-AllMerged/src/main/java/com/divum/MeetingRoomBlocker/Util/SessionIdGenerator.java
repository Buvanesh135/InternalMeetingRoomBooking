package com.divum.MeetingRoomBlocker.Util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SessionIdGenerator {
    public String getRandomId(){
        return UUID.randomUUID().toString();
    }
}
