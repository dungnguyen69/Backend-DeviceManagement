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
	@NotNull
	@NotEmpty
	private int statusId;
	@NotNull
	@NotEmpty
	private int platformId;
	@NotNull
	@NotEmpty
	private int itemTypeId;
	@NotNull
	@NotEmpty
	private int ramId;
	@NotNull
	@NotEmpty
	private int screenId;
	@NotNull
	@NotEmpty
	private int storageId;
	@NotNull
	@NotEmpty
	private int ownerId;
	@NotNull
	@NotEmpty(message = "Inventory Number is mandatory.")
	private String inventoryNumber;
	@NotNull
	@NotEmpty(message = "Serial Number is mandatory.")
	private String serialNumber;
	@NotNull
	@NotEmpty
	private int originId;
	@NotNull
	@NotEmpty
	private int projectId;
	private String comments;
	private Date createdDate;
}
