package com.fullstack.Backend.repositories.impl;

import java.util.List;

import org.springframework.stereotype.Repository;
import com.fullstack.Backend.entities.Device;
import com.fullstack.Backend.repositories.interfaces.IDeviceRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

@Repository
public class DeviceRepository implements IDeviceRepository {

	EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("Device");
	EntityManager entityManager = entityManagerFactory.createEntityManager();
	private final String SQL_AllDevices = "SELECT d.id, d.comments, d.created_date, d.inventory_number, \r\n"
			+ "d.name, d.origin, d.project, d.serial_number, \r\n"
			+ "d.status, d.created_date, i.name, u.user_name,\r\n"
			+ "p.name, p.version, r.size, s.size, st.size from devices d \r\n"
			+ "join item_types i on d.item_type_id = i.id\r\n" + "join users u on d.owner_id = u.id\r\n"
			+ "join platform p on d.platform_id = p.id\r\n" + "join rams r on d.item_type_id = r.id\r\n"
			+ "join screens s on d.item_type_id = s.id\r\n" + "join storages st on d.item_type_id = st.id\r\n";

	@Override
	public List<Device> getAllDevices() {
		Query query = entityManager.createQuery(SQL_AllDevices);
		@SuppressWarnings("unchecked")
		List<Device> listDevices = query.getResultList();
		return listDevices;
	}

	@Override
	public Device getDetailDevice(int deviceId) {
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
