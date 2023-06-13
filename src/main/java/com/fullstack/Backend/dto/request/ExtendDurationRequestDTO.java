package com.fullstack.Backend.dto.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ExtendDurationRequestDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date returnDate;
    private int deviceId;
    private String nextKeeper;
}
