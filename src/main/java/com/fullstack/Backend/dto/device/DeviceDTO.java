package com.fullstack.Backend.dto.device;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fullstack.Backend.entities.Device;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
	public Long RamSize;
	@JsonProperty("ScreenSize")
	public Long ScreenSize;
	@JsonProperty("StorageSize")
	public Long StorageSize;
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
	@JsonProperty("BookingDate")
	public Date BookingDate;
	@JsonProperty("ReturnDate")
	public Date ReturnDate;
	@JsonProperty("CreatedDate")
	public Date CreatedDate;
	@JsonProperty("UpdatedDate")
	public Date UpdatedDate;

	public void loadFromEntity(Device device) {
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
		this.Owner = device.getOwner().getUserName();
		this.CreatedDate = device.getCreatedDate();
		this.UpdatedDate = device.getUpdatedDate();
	}
}
