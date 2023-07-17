package com.fullstack.Backend.responses.users;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class MessageResponse {
    private String message;
    private Date timeStamp;
    private String status;
    public MessageResponse(String message){
        this.message = message;
        this.timeStamp = new Date();
    }
}
