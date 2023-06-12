package com.fullstack.Backend.repositories.interfaces;

import java.util.List;

import com.fullstack.Backend.entities.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface IRequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
    public static final String FIND_ALL_REQUESTS_BY_EMPLOYEE_ID = "SELECT * FROM requests WHERE "
            + "current_keeper_id = :employeeId "
            + "OR next_keeper_id = :employeeId "
            + "OR requester_Id = :employeeId "
            + "ORDER BY :sortBy :sortDir";
    public static final String FIND_IDENTICAL_DEVICE_RELATED_PENDING_REQUESTS = "SELECT * FROM requests WHERE "
            + "id != :requestId "
            + "AND current_keeper_id = :currentKeeperId "
            + "AND device_id = :deviceId "
            + "AND request_status = :requestStatus";
    public static final String FIND_REPETITIVE_REQUESTS = "SELECT * FROM requests WHERE "
            + "requester_id = :requesterId "
            + "AND current_keeper_id = :currentKeeperId "
            + "AND next_keeper_id = :nextKeeperId "
            + "AND device_id = :deviceId "
            + "AND request_status IN (0,4)";

    @Query(value = FIND_ALL_REQUESTS_BY_EMPLOYEE_ID, nativeQuery = true)
    public List<Request> findAllRequest(int employeeId, String sortBy, String sortDir);

    @Query(value = FIND_IDENTICAL_DEVICE_RELATED_PENDING_REQUESTS, nativeQuery = true)
    public List<Request> findDeviceRelatedApprovedRequest(int requestId, int currentKeeperId, int deviceId, int requestStatus);

    @Query(value = FIND_REPETITIVE_REQUESTS, nativeQuery = true)
    public Request findRepetitiveRequest(int requesterId, int currentKeeperId, int nextKeeperId, int deviceId);
}
