package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import com.fullstack.Backend.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IRequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
    public static final String FIND_ALL_REQUESTS_BY_EMPLOYEE_ID = "SELECT * FROM requests WHERE " +
            "current_keeper_id = :employeeId " +
            "OR next_keeper_id = :employeeId " +
            "OR requester_Id = :employeeId";

    @Query(value = FIND_ALL_REQUESTS_BY_EMPLOYEE_ID, nativeQuery = true)
    public List<Request> findAllRequest(int employeeId);

//	public void createNewRequest(Request request);
//
//	public List<Request> getListRequestByEmployeeId(int employeeId);
//
//	public Request findRequestById(int id);
//
//	public List<Request> findRequestRelatedDeviceApproved(int requestApprovedId, int currentKeeperId, int deviceId,
//			String status);
}
