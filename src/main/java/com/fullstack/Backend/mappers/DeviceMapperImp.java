package com.fullstack.Backend.mappers;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.DeviceDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class DeviceMapperImp implements DeviceMapper {
    @Override
    public DeviceDTO deviceToDeviceDto(Device device) {
        DeviceDTO dto = new DeviceDTO();
        if (device.getOwner() != null)
            dto.setOwner(device.getOwner().getUserName());

        dto.setId(device.getId());
        dto.setDeviceName(device.getName());
        dto.setItemType(device.getItemType().getName());
        dto.setStatus(device.getStatus().name());
        dto.setPlatformName(device.getPlatform().getName());
        dto.setPlatformVersion(device.getPlatform().getVersion());
        dto.setRamSize(device.getRam().getSize());
        dto.setScreenSize(device.getScreen().getSize());
        dto.setStorageSize(device.getStorage().getSize());
        dto.setInventoryNumber(device.getInventoryNumber());
        dto.setSerialNumber(device.getSerialNumber());
        dto.setComments(device.getComments());
        dto.setProject(device.getProject().name());
        dto.setOrigin(device.getOrigin().name());
        dto.setCreatedDate(device.getCreatedDate());
        dto.setUpdatedDate(device.getUpdatedDate());

        return dto;
    }

    @Override
    public Device addDeviceDtoToDevice(AddDeviceDTO dto) {
        Device device = new Device();
        device.setName(dto.getDeviceName());
        device.setItemTypeId(dto.getItemTypeId());
        device.setStatus(Status.values()[dto.getStatusId()]);
        device.setPlatformId(dto.getPlatformId());
        device.setRamId(dto.getRamId());
        device.setScreenId(dto.getScreenId());
        device.setStorageId(dto.getStorageId());
        device.setInventoryNumber(dto.getInventoryNumber());
        device.setSerialNumber(dto.getSerialNumber());
        device.setComments(dto.getComments());
        device.setProject(Project.values()[dto.getProjectId()]);
        device.setOrigin(Origin.values()[dto.getOriginId()]);
        device.setCreatedDate(new Date());
        return device;
    }
}
