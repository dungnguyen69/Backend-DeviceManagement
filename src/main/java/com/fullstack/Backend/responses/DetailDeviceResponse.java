package com.fullstack.Backend.responses;

import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.entities.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDeviceResponse {
	DeviceUpdateDTO detailDevice;
}
