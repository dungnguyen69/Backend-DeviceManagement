package com.fullstack.Backend.dto.device;

import java.util.Date;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateDeviceDTO {
    private int id;
    @NotEmpty
    private String name;
    private int statusId;
    private int platformId;
    private int itemTypeId;
    private int ramId;
    private int screenId;
    private int storageId;
    private Integer ownerId;
    private Integer keeperId;
    @NotEmpty
    private String inventoryNumber;
    @NotEmpty
    private String serialNumber;
    private int originId;
    private int projectId;
    private String comments;
    private Date createdDate;
    private Date updatedDate;
    private Date bookingDate;
    private Date returnDate;

    public void loadFromEntity(Device device) {
        this.id = device.getId();
        this.name = device.getName();
        this.itemTypeId = device.getItemTypeId();
        this.statusId = Status.valueOf(device.getStatus().toString()).ordinal();
        this.platformId = device.getPlatformId();
        this.ramId = device.getRamId();
        this.screenId = device.getScreenId();
        this.storageId = device.getStorageId();
        this.inventoryNumber = device.getInventoryNumber();
        this.serialNumber = device.getSerialNumber();
        this.comments = device.getComments();
        this.projectId = Project.valueOf(device.getProject().toString()).ordinal();
        this.originId = Origin.valueOf(device.getOrigin().toString()).ordinal();
        this.createdDate = device.getCreatedDate();
        this.updatedDate = device.getUpdatedDate();
        this.bookingDate = device.getBookingDate();
        this.returnDate = device.getReturnDate();
    }
}
