package com.fullstack.Backend.responses.device;

import java.util.List;

import com.fullstack.Backend.dto.device.DeviceDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceInWarehouseResponse {
	private List<DeviceDTO> devicesList;
	private List<String> statusList;
	private List<String> originList;
	private List<String> projectList;
	private List<String> itemTypeList;
	private List<String> keeperNumberOptions;
	private int pageNo;
	private int pageSize;
	private long totalElements;
	private int totalPages;
}
