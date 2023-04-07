package com.fullstack.Backend.responses;

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
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
