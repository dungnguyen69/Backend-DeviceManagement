package com.fullstack.Backend.responses;

import com.fullstack.Backend.dto.device.DeviceUpdateDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDeviceResponse {
	DeviceUpdateDTO detailDevice;
}
