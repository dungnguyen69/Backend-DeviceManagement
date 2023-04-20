package com.fullstack.Backend.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFilterDTO {
	private String name;
	private String status;
	private String platformName;
	private String platformVersion;
	private String itemType;
	private String ram;
	private String screen;
	private String storage;
	private String owner;
	private String inventoryNumber;
	private String serialNumber;
	private String origin;
	private String project;
	public void formatFilter() {
		this.name = this.name.trim().toLowerCase();
		this.status = this.status.trim().toLowerCase();
		this.platformName = this.platformName.trim().toLowerCase();
		this.platformVersion = this.platformVersion.trim().toLowerCase();
		this.itemType = this.itemType.trim().toLowerCase();
		this.ram = this.ram.trim().toLowerCase();
		this.screen = this.screen.trim().toLowerCase();
		this.storage = this.storage.trim().toLowerCase();
		this.owner = this.owner.trim().toLowerCase();
		this.inventoryNumber = this.inventoryNumber.trim().toLowerCase();
		this.serialNumber = this.serialNumber.trim().toLowerCase();
		this.origin = this.origin.trim().toLowerCase();
		this.project = this.project.trim().toLowerCase();
	}
}
