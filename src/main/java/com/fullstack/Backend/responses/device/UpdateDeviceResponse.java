package com.fullstack.Backend.responses.device;

import com.fullstack.Backend.entities.Device;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceResponse {
	Device updatedDevice;
}
