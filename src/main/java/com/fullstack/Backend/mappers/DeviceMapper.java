package com.fullstack.Backend.mappers;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.entities.Device;
import org.mapstruct.Mapper;

@Mapper
public interface DeviceMapper {
    DeviceDTO deviceToDeviceDto(Device device);

    Device addDeviceDtoToDevice(AddDeviceDTO dto);
}
