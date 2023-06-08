package com.fullstack.Backend.responses.device;

import com.fullstack.Backend.dto.device.UpdateDeviceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDeviceResponse {
	UpdateDeviceDTO detailDevice;
}
