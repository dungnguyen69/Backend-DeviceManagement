package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface IDeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
    public static final String FIND_DEVICE_BY_SERIALNUMBER = "SELECT * FROM devices WHERE serial_number = ?";
    public static final String FIND_DEVICE_BY_SERIALNUMBER_EXCEPT_PROVIDED_DEVICE = "SELECT * FROM devices WHERE serial_number = :serialNumber AND id != :deviceId";

    public Device findById(int deviceId);

    // For update device information when importing
    @Query(value = FIND_DEVICE_BY_SERIALNUMBER, nativeQuery = true)
    public Device findBySerialNumber(String serialNumber);

    @Query(value = FIND_DEVICE_BY_SERIALNUMBER_EXCEPT_PROVIDED_DEVICE, nativeQuery = true)
    public Device findBySerialNumberExceptProvidedDevice(int deviceId, String serialNumber);

    public List<Device> findByOwnerId(int ownerId, Sort sort);
}
