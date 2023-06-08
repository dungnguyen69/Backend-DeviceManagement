package com.fullstack.Backend.services;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.fullstack.Backend.dto.device.AddDeviceDTO;
import com.fullstack.Backend.dto.device.FilterDeviceDTO;
import com.fullstack.Backend.dto.device.UpdateDeviceDTO;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.responses.device.AddDeviceResponse;
import com.fullstack.Backend.responses.device.DeleteDeviceResponse;
import com.fullstack.Backend.responses.device.DetailDeviceResponse;
import com.fullstack.Backend.responses.device.DeviceInWarehouseResponse;
import com.fullstack.Backend.responses.device.DropdownValuesResponse;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.device.UpdateDeviceResponse;
import com.fullstack.Backend.utils.dropdowns.OriginList;
import com.fullstack.Backend.utils.dropdowns.ProjectList;
import com.fullstack.Backend.utils.dropdowns.StatusList;

import jakarta.servlet.http.HttpServletResponse;

public interface IDeviceService {
	public CompletableFuture<DeviceInWarehouseResponse> showDevicesWithPaging(int pageIndex, int pageSize,
			String sortBy, String sortDir, FilterDeviceDTO deviceFilterDTO)
			throws InterruptedException, ExecutionException;

	public CompletableFuture<AddDeviceResponse> addANewDevice(AddDeviceDTO device);

	public CompletableFuture<DetailDeviceResponse> getDetailDevice(int deviceId)
			throws InterruptedException, ExecutionException;

	public CompletableFuture<UpdateDeviceResponse> updateDevice(int deviceId, UpdateDeviceDTO device);

	public void formatFilter(FilterDeviceDTO deviceFilterDTO);

	public CompletableFuture<DeleteDeviceResponse> deleteADevice(int deviceId);

	public void exportToExcel(HttpServletResponse response) throws IOException;

	public void downloadTemplate(HttpServletResponse response)
			throws IOException, InterruptedException, ExecutionException;

	public CompletableFuture<ResponseEntity<Object>> importToDb(MultipartFile file) throws IOException;

	public CompletableFuture<KeywordSuggestionResponse> getSuggestKeywordDevices(int fieldColumn, String keyword,
                                                                                 FilterDeviceDTO deviceFilter) throws InterruptedException, ExecutionException;

	public int getTotalPages(int pageSize, int listSize);

	public CompletableFuture<List<Device>> fetchFilteredDevice(FilterDeviceDTO deviceFilter, List<Device> devices);

	public CompletableFuture<List<Device>> getPage(List<Device> sourceList, int pageIndex, int pageSize);

	public CompletableFuture<DropdownValuesResponse> getDropDownValues()
			throws InterruptedException, ExecutionException;

	public CompletableFuture<List<StatusList>> getStatusList();

	public CompletableFuture<List<ProjectList>> getProjectList();

	public CompletableFuture<List<OriginList>> getOriginList();
}
