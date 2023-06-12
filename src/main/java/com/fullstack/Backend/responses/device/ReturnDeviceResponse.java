package com.fullstack.Backend.responses.device;
import lombok.Data;

import java.util.List;

@Data
public class ReturnDeviceResponse {
    private boolean isKeepDeviceReturned = false;
    private List<String> oldKeepers;
}
