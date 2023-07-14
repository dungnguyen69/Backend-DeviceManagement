package com.fullstack.Backend.dto.device;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.Backend.entities.*;
import com.fullstack.Backend.enums.Origin;
import com.fullstack.Backend.enums.Project;
import com.fullstack.Backend.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class KeepingDeviceDTO {
    @JsonProperty("Id")
    public int Id;
    @JsonProperty("DeviceName")
    public String DeviceName;
    @JsonProperty("Status")
    public String Status;
    @JsonProperty("ItemType")
    public String ItemType;
    @JsonProperty("PlatformName")
    public String PlatformName;
    @JsonProperty("PlatformVersion")
    public String PlatformVersion;
    @JsonProperty("RamSize")
    public String RamSize;
    @JsonProperty("ScreenSize")
    public String ScreenSize;
    @JsonProperty("StorageSize")
    public String StorageSize;
    @JsonProperty("InventoryNumber")
    public String InventoryNumber;
    @JsonProperty("SerialNumber")
    public String SerialNumber;
    @JsonProperty("Comments")
    public String Comments;
    @JsonProperty("Project")
    public String Project;
    @JsonProperty("Origin")
    public String Origin;
    @JsonProperty("Owner")
    public String Owner;
    @JsonProperty("Keeper")
    public String Keeper;
    @JsonProperty("KeeperNo")
    public int KeeperNo;
    @JsonProperty("BookingDate")
    public Date BookingDate;
    @JsonProperty("ReturnDate")
    public Date ReturnDate;
    @JsonProperty("MaxExtendingReturnDate")
    public Date MaxExtendingReturnDate;
    @JsonProperty("isReturnable")
    public Boolean isReturnable = true;
    @JsonProperty("CreatedDate")
    public Date CreatedDate;
    @JsonProperty("UpdatedDate")
    public Date UpdatedDate;

    public KeepingDeviceDTO(Device device) {
        if (device.getOwner() == null)
            this.Owner = "";
        else
            this.Owner = device.getOwner().getUserName();

        this.Id = device.getId();
        this.DeviceName = device.getName();
        this.ItemType = device.getItemType().getName();
        this.Status = device.getStatus().name();
        this.PlatformName = device.getPlatform().getName();
        this.PlatformVersion = device.getPlatform().getVersion();
        this.RamSize = device.getRam().getSize();
        this.ScreenSize = device.getScreen().getSize();
        this.StorageSize = device.getStorage().getSize();
        this.InventoryNumber = device.getInventoryNumber();
        this.SerialNumber = device.getSerialNumber();
        this.Comments = device.getComments();
        this.Project = device.getProject().name();
        this.Origin = device.getOrigin().name();
        this.CreatedDate = device.getCreatedDate();
        this.UpdatedDate = device.getUpdatedDate();
    }
}
