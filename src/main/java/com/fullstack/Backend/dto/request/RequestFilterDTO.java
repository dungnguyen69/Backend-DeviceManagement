package com.fullstack.Backend.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFilterDTO {
    private String requestId;
    private String currentKeeper;
    private String nextKeeper;
    private String device;
    private String requester;
    private String requestStatus;
    private Date bookingDate;
    private Date returnDate;
}
