package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Device;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {
    public static final String FIND_DEVICE_BY_SERIALNUMBER = """
            SELECT d FROM Device d WHERE serialNumber = :serialNumber
            """;
    public static final String FIND_DEVICE_BY_SERIALNUMBER_EXCEPT_UPDATED_DEVICE = """
            SELECT d FROM Device d WHERE serialNumber = :serialNumber AND Id <> :deviceId
            """;

    public Optional<Device> findById(int deviceId);

    // For update device information when importing
    @Query(FIND_DEVICE_BY_SERIALNUMBER)
    public Device findBySerialNumber(String serialNumber);

    @Query(FIND_DEVICE_BY_SERIALNUMBER_EXCEPT_UPDATED_DEVICE)
    public Device findBySerialNumberExceptUpdatedDevice(int deviceId, String serialNumber);

    public List<Device> findByOwnerId(int ownerId, Sort sort);
}
