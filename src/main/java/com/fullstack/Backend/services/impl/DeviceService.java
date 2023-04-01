package com.fullstack.Backend.services.impl;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;
import com.fullstack.Backend.services.IDeviceService;

@Service
public class DeviceService implements IDeviceService{
	
	@Autowired
	IDeviceRepository deviceRepository;
	@Override
	public List<Device> getAllDevices() {
        List<Device> devices = deviceRepository.getAllDevices();
		return devices;
	}

	@Override
	public Device getDetailDevice(int deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device updateDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device filterDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
