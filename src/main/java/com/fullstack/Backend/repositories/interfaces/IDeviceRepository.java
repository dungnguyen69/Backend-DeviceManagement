package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface IDeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
    public static final String FIND_DEVICE_BY_SERIALNUMBER = "SELECT d FROM Device d WHERE serialNumber = :serialNumber";

    public Device findById(int deviceId);

    // For update device information when importing
    @Query(FIND_DEVICE_BY_SERIALNUMBER)
    public Device findBySerialNumber(String serialNumber);

    public List<Device> findByOwnerId(int ownerId, Sort sort);
}
