package com.fullstack.Backend.dto.device;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FilterDeviceDTO {
	private String name;
	private String status;
	private String platformName;
	private String platformVersion;
	private String itemType;
	private String ram;
	private String screen;
	private String storage;
	private String owner;
	private String keeper;
	private String inventoryNumber;
	private String serialNumber;
	private String origin;
	private String project;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date bookingDate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date returnDate;
}
