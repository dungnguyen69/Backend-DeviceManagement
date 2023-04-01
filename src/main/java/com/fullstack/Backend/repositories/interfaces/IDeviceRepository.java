package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import com.fullstack.Backend.entities.Device;

public interface IDeviceRepository {
	
	public List<Device> getAllDevices();
	public Device getDetailDevice(Long Id);
	public Device updateDevice();
	public Device filterDevice();
	
}
