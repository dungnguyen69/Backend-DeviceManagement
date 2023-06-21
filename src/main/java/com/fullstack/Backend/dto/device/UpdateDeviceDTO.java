package com.fullstack.Backend.dto.device;

import java.util.Date;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateDeviceDTO {
    private int id;
    @NotEmpty
    private String name;
    @NotEmpty
    private int statusId;
    @NotEmpty
    private int platformId;
    @NotEmpty
    private int itemTypeId;
    @NotEmpty
    private int ramId;
    @NotEmpty
    private int screenId;
    @NotEmpty
    private int storageId;
    @NotNull
    @NotEmpty
    private String inventoryNumber;
    @NotNull
    @NotEmpty
    private String serialNumber;
    @NotEmpty
    private int originId;
    @NotEmpty
    private int projectId;
    private Integer ownerId;
    private Integer keeperId;
    private String comments;
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
        this.bookingDate = device.getBookingDate();
        this.returnDate = device.getReturnDate();
    }
}
