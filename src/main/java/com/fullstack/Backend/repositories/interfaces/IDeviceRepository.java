package com.fullstack.Backend.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fullstack.Backend.entities.Device;

public interface IDeviceRepository extends JpaRepository<Device, Long> {
	public Device findById(int deviceId);
}
