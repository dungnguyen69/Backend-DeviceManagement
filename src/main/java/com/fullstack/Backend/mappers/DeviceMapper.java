package com.fullstack.Backend.mappers;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.dto.device.UpdateDeviceDTO;
import com.fullstack.Backend.entities.Device;
import org.mapstruct.Mapper;

import java.util.Optional;

@Mapper
public interface DeviceMapper {
    DeviceDTO deviceToDeviceDto(Device device);

    Device addDeviceDtoToDevice(AddDeviceDTO dto);

    Optional<Device> updateDtoToDevice(Device deviceDetail, UpdateDeviceDTO dto);
}
