package com.fullstack.Backend.utils;

import lombok.*;

import java.util.Date;

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

    private String deviceName;

    private String platformName;

    private String platformVersion;

    private String itemType;

    private String ramSize;

    private String screenSize;

    private String storageSize;

    private String inventoryNumber;

    private String serialNumber;

    private String errorMessage;
}
