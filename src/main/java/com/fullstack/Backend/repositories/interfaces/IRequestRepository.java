package com.fullstack.Backend.repositories.interfaces;

import java.util.List;
import com.fullstack.Backend.entities.Request;

public interface IRequestRepository {
	public void createNewRequest(Request request);

	public List<Request> getListRequestByEmployeeId(int employeeId);

	public Request findRequestById(int id);

	public List<Request> findRequestRelatedDeviceApproved(int requestApprovedId, int currentKeeperId, int deviceId,
			String status);
}
