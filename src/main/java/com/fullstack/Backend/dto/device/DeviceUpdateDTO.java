package com.fullstack.Backend.dto.device;

import java.util.Date;

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
	private int ownerId;
	@NotEmpty
	private String inventoryNumber;
	@NotEmpty
	private String serialNumber;
	private int originId;
	private int projectId;
	private String comments;
	@NotEmpty
	private Date createdDate;
	@NotEmpty
	private Date updatedDate;
}
