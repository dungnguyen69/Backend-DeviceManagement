package com.fullstack.Backend.dto.device;

import java.util.Date;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddDeviceDTO {
	@NotEmpty(message = "Name is mandatory.")
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
	@NotEmpty
	private int ownerId;
	@NotNull
	@NotEmpty(message = "Inventory Number is mandatory.")
	private String inventoryNumber;
	@NotNull
	@NotEmpty(message = "Serial Number is mandatory.")
	private String serialNumber;
	@NotEmpty
	private int originId;
	@NotEmpty
	private int projectId;
	private String comments;
	private Date createdDate;
}
