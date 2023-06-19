package com.fullstack.Backend.dto.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReturnKeepDeviceDTO {
    @NotNull
    @NotEmpty
    private int keeperNo;
    @NotNull
    @NotEmpty
    private int deviceId;
    @NotNull
    @NotEmpty
    private int currentKeeperId;
}
