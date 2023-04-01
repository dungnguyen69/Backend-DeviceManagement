package com.fullstack.Backend.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fullstack.Backend.entities.Device;

public interface IDeviceService {
	public List<Device> getAllDevices();

	public Device getDetailDevice(int deviceId);

	public Device updateDevice();

	public Device filterDevice();
}
