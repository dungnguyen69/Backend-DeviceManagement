package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.utils.dropdowns.ItemTypeList;

public interface IDeviceRepository extends JpaRepository<Device, Long>, JpaSpecificationExecutor<Device> {
	public static final String FIND_DEVICE_BY_SERIALNUMBER = "SELECT * FROM devices WHERE serial_number = ?";
	public static final String RETURN_ITEMTYPE_LIST = "SELECT DISTINCT it.id, it.name FROM devices d JOIN item_types it WHERE d.item_type_id = it.id";

	public Device findById(int deviceId);

	// For update device information when importing
	@Query(value = FIND_DEVICE_BY_SERIALNUMBER, nativeQuery = true)
	public Device findBySerialNumber(String serialNumber);

	// Join tables and return a list
	@Query(value = RETURN_ITEMTYPE_LIST, nativeQuery = true)
	public List<ItemTypeList> fetchItemTypeList(String serialNumber);

	@Query(value = RETURN_ITEMTYPE_LIST, nativeQuery = true)
	public Device fetchStatusList(String serialNumber);

	@Query(value = RETURN_ITEMTYPE_LIST, nativeQuery = true)
	public Device fetchProjectList(String serialNumber);

	@Query(value = RETURN_ITEMTYPE_LIST, nativeQuery = true)
	public Device fetchOriginList(String serialNumber);

}
