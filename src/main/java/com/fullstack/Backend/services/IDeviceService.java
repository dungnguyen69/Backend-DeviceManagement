package com.fullstack.Backend.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.Backend.dto.device.DeviceAddDTO;
import com.fullstack.Backend.dto.device.DeviceFilterDTO;
import com.fullstack.Backend.dto.device.DeviceUpdateDTO;
import com.fullstack.Backend.responses.AddDeviceResponse;
import com.fullstack.Backend.responses.DeleteDeviceResponse;
import com.fullstack.Backend.responses.DetailDeviceResponse;
import com.fullstack.Backend.responses.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.FilterDeviceResponse;
import com.fullstack.Backend.responses.UpdateDeviceResponse;

import jakarta.servlet.http.HttpServletResponse;

public interface IDeviceService {
	public CompletableFuture<DeviceInWarehouseResponse> showDevicesWithPaging(int pageIndex, int pageSize,
			String sortBy, String sortDir, DeviceFilterDTO deviceFilterDTO);

	public CompletableFuture<AddDeviceResponse> addANewDevice(DeviceAddDTO device);

	public CompletableFuture<DetailDeviceResponse> getDetailDevice(int deviceId)
			throws InterruptedException, ExecutionException;

	public CompletableFuture<UpdateDeviceResponse> updateDevice(int deviceId, DeviceUpdateDTO device);

	public void formatFilter(DeviceFilterDTO deviceFilterDTO);

	public CompletableFuture<FilterDeviceResponse> getSuggestKeywordDevices(int fieldColumn, String keyword,
			DeviceFilterDTO deviceFilter);

	public CompletableFuture<DeleteDeviceResponse> deleteADevice(int deviceId);

	public void exportToExcel(HttpServletResponse response) throws IOException;

	public void downloadTemplate(HttpServletResponse response)
			throws IOException, InterruptedException, ExecutionException;

	public CompletableFuture<ResponseEntity<Object>> importToDb(MultipartFile file) throws IOException;
}
