package com.fullstack.Backend.dto.device;

import java.util.Date;
import org.hibernate.annotations.CreationTimestamp;

import com.fullstack.Backend.entities.ItemType;
import com.fullstack.Backend.entities.Platform;
import com.fullstack.Backend.entities.Ram;
import com.fullstack.Backend.entities.Screen;
import com.fullstack.Backend.entities.Storage;
import com.fullstack.Backend.entities.User;
import com.fullstack.Backend.entities.origin;
import com.fullstack.Backend.entities.project;
import com.fullstack.Backend.entities.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFilterDTO {
	private Long id;
	private String name;
	private Integer statusId;
	private Integer platformId;
	private Integer itemTypeId;
	private Integer ramId;
	private Integer screenId;
	private Integer storageId;
	private Integer ownerId;
	private String inventoryNumber;
	private String serialNumber;
	private Integer originId;
	private Integer projectId;
	private String comments;
	private Date bookingDate;
	private Date dueDate;
}
