package com.fullstack.Backend.responses.device;

import com.fullstack.Backend.dto.device.UpdateDeviceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailDeviceResponse implements Serializable {
	UpdateDeviceDTO detailDevice;
}
