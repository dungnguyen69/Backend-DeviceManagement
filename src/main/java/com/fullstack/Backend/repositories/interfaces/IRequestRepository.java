package com.fullstack.Backend.repositories.interfaces;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fullstack.Backend.entities.Request;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IRequestRepository extends JpaRepository<Request, Long>, JpaSpecificationExecutor<Request> {
    public static final String FIND_ALL_REQUESTS_BY_EMPLOYEE_ID = "SELECT r FROM Request r WHERE "
            + "r.currentKeeper_Id = :employeeId "
            + "OR r.nextKeeper_Id = :employeeId "
            + "OR r.requester_Id = :employeeId";
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

    public static final String FIND_AN_OCCUPIED_REQUEST = "SELECT r FROM Request r WHERE "
            + "r.nextKeeper_Id = :nextKeeperId "
            + "AND r.device_Id = :deviceId "
            + "AND r.requestStatus = 3";

    @Query(FIND_ALL_REQUESTS_BY_EMPLOYEE_ID)
    public List<Request> findAllRequest(@Param("employeeId") int employeeId, Sort sort);

    @Query(value = FIND_IDENTICAL_DEVICE_RELATED_PENDING_REQUESTS, nativeQuery = true)
    public List<Request> findDeviceRelatedApprovedRequest(int requestId, int currentKeeperId, int deviceId, int requestStatus);

    @Query(value = FIND_REPETITIVE_REQUESTS, nativeQuery = true)
    public Request findRepetitiveRequest(int requesterId, int currentKeeperId, int nextKeeperId, int deviceId);

    @Query(FIND_AN_OCCUPIED_REQUEST)
    public Request findAnOccupiedRequest(int nextKeeperId, int deviceId);
}
