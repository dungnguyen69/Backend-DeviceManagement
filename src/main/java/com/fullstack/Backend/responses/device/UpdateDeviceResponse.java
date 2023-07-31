package com.fullstack.Backend.responses.device;

import com.fullstack.Backend.entities.Device;

import com.fullstack.Backend.utils.ErrorMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateDeviceResponse {
	Device updatedDevice;
	List<ErrorMessage> errors;
}
