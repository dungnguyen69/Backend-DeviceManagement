package com.fullstack.Backend.dto.device;

import java.util.Date;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class AddDeviceDTO {
	private String deviceName;
	private int statusId;
	private int platformId;
	private int itemTypeId;
	private int ramId;
	private int screenId;
	private int storageId;
	private String owner;
	private String inventoryNumber;
	private String serialNumber;
	private int originId;
	private int projectId;
	private String comments;
}
