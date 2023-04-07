package com.fullstack.Backend.dto.device;

import java.util.Date;

import org.hibernate.validator.constraints.UniqueElements;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DeviceAddDTO {
	@NotEmpty(message = "Name is mandatory.")
	private String name;
	private int statusId;
	private int platformId;
	private int itemTypeId;
	private int ramId;
	private int screenId;
	private int storageId;
	private int ownerId;
	@NotEmpty(message = "Inventory Number is mandatory.")
	private String inventoryNumber;
	@NotEmpty(message = "Serial Number is mandatory.")
	private String serialNumber;
	private int originId;
	private int projectId;
	private String comments;
	private Date createdDate;
}
