package com.fullstack.Backend.dto.device;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.Backend.entities.Device;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@Builder(toBuilder = true)
@AllArgsConstructor
public class DeviceDTO {
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
    @JsonProperty("KeeperNumber")
    public int keeperNumber;
    @JsonProperty("BookingDate")
    public Date BookingDate;
    @JsonProperty("ReturnDate")
    public Date ReturnDate;
    @JsonProperty("CreatedDate")
    public Date CreatedDate;
    @JsonProperty("UpdatedDate")
    public Date UpdatedDate;
}
