package com.fullstack.Backend.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
public class ExtendDurationRequestDTO {
    private Date returnDate;
    @NotEmpty
    private int deviceId;
    @NotNull
    @NotEmpty
    private String nextKeeper;
}
