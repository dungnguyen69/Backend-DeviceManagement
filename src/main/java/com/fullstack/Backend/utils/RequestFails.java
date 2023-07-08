package com.fullstack.Backend.utils;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestFails {
    private String requester;

    private String currentKeeper;

    private String nextKeeper;

    private Date bookingDate;

    private Date returnDate;

    private int deviceId;

    private String deviceName;

    private List<String> errorMessage;
}
