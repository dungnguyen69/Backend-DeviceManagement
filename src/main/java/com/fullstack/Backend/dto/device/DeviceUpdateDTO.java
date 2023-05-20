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
@AllArgsConstructor
public class DeviceUpdateDTO {
	private int id;
	@NotEmpty
	private String name;
	private int statusId;
	private int platformId;
	private int itemTypeId;
	private int ramId;
	private int screenId;
	private int storageId;
	private String ownerId;
	@NotEmpty
	private String inventoryNumber;
	@NotEmpty
	private String serialNumber;
	private int originId;
	private int projectId;
	private String comments;
	private Date createdDate;
	private Date updatedDate;

	public void loadFromEntity(Device device) {
		this.id = device.getId();
		this.name = device.getName();
		this.itemTypeId = device.getItem_type_Id();
		this.statusId = Status.valueOf(device.getStatus().toString()).ordinal();
		this.platformId = device.getPlatform_Id();
		this.ramId = device.getRam_Id();
		this.screenId = device.getScreen_Id();
		this.storageId = device.getStorage_Id();
		this.inventoryNumber = device.getInventoryNumber();
		this.serialNumber = device.getSerialNumber();
		this.comments = device.getComments();
		this.projectId = Project.valueOf(device.getProject().toString()).ordinal();
		this.originId = Origin.valueOf(device.getOrigin().toString()).ordinal();
		this.createdDate = device.getCreatedDate();
		this.updatedDate = device.getUpdatedDate();
	}
}
