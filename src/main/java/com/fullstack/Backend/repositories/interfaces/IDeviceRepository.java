package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.fullstack.Backend.entities.Device;

public interface IDeviceRepository {
	
	public List<Device> getAllDevices();
	public Device getDetailDevice(int deviceId);
	public Device updateDevice();
	public Device filterDevice();
	
}
