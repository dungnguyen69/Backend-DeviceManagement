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
    private String approver;
    private String currentKeeper;
    private String nextKeeper;
    private String device;
    private String serialNumber;
    private String requester;
    private String requestStatus;
    private Date bookingDate;
    private Date returnDate;
}
