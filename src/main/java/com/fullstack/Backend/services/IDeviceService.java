package com.fullstack.Backend.services;

import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;

public interface IDeviceService {
	public DeviceInWarehouseResponse getAllDevices(int pageIndex, int pageSize, String sortBy, String sortDir);

	public AddDeviceResponse addANewDevice(DeviceAddDTO device);
	
	public DetailDeviceResponse getDetailDevice(int deviceId);

	public Device updateDevice();

	public Device filterDevice();
}
