package com.fullstack.Backend.services;

import java.util.List;

import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.FilterDeviceResponse;
import com.fullstack.Backend.responses.UpdateDeviceResponse;

public interface IDeviceService {
	public DeviceInWarehouseResponse getAllDevicesWithPaging(int pageIndex, int pageSize, String sortBy, String sortDir,
			DeviceFilterDTO deviceFilterDTO);

	public AddDeviceResponse addANewDevice(DeviceAddDTO device);

	public DetailDeviceResponse getDetailDevice(int deviceId);

	public UpdateDeviceResponse updateDevice(int deviceId, DeviceUpdateDTO device);

//	public List<Device> filterDevice(DeviceFilterDTO deviceFilterDTO, List<Device> devices);

	public void formatFilter(DeviceFilterDTO deviceFilterDTO);

	public FilterDeviceResponse getSuggestKeywordDevices(int fieldColumn, String keyword, DeviceFilterDTO deviceFilter);
}
