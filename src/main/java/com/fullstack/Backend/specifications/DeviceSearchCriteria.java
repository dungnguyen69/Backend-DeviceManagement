package com.fullstack.Backend.specifications;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeviceSearchCriteria {
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
}
