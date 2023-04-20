package com.fullstack.Backend.repositories.interfaces;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.fullstack.Backend.entities.Device;

public interface IDeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
	public Device findById(int deviceId);
}
