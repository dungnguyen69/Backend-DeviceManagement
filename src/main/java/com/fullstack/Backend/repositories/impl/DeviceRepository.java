package com.fullstack.Backend.repositories.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;


@Repository
public class DeviceRepository implements IDeviceRepository {

	@PersistenceContext
	EntityManager entityManager;
	private final String SQL_AllDevices = "SELECT d.Id, d.comments, d.createdDate, d.inventoryNumber, \r\n"
			+ "d.name, d.origin, d.project, d.serialNumber, \r\n"
			+ "d.status, i.name, u.userName,\r\n"
			+ "p.name, p.version, r.size, s.size, st.size from Device d \r\n"
			+ "join platform p on d.platform_Id = p.Id\r\n" + "join Ram r on d.ram_Id = r.Id\r\n"
			+ "join ItemType i on d.itemType_Id = i.Id\r\n" + "join User u on d.owner_Id = u.Id\r\n"
			+ "join Screen s on d.screen_Id = s.Id\r\n" + "join Storage st on d.storage_Id = st.Id\r\n";

	@Override
	public List<Device> getAllDevices() {
		Query query = entityManager.createQuery(SQL_AllDevices);
		@SuppressWarnings("unchecked")
		List<Device> listDevices = (List<Device>) query.getResultList();
		return listDevices;
	}

	@Override
	public Device getDetailDevice(Long deviceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device updateDevice() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Device filterDevice() {
		// TODO Auto-generated method stub
		return null;
	}

}
