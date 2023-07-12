package com.fullstack.Backend.repositories.interfaces;

import com.fullstack.Backend.entities.KeeperOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IKeeperOrderRepository extends JpaRepository<KeeperOrder, Long> {
    public static final String FIND_KEEPER_ORDER_LIST_BY_DEVICE_ID = "SELECT ko FROM KeeperOrder ko WHERE device_Id = :deviceId AND isReturned = false";
    public static final String FIND_KEEPER_ORDER_BY_DEVICE_ID_AND_KEEPER_ID = "SELECT ko FROM KeeperOrder ko WHERE device_Id = :deviceId AND keeper_Id = :keeperId AND isReturned = false";
    public static final String FIND_KEEPER_ORDER_LIST_BY_KEEPER_ID = "SELECT ko FROM KeeperOrder ko WHERE keeper_Id = :keeperId AND isReturned = false";
    public static final String FIND_KEEPER_ORDER_LIST_BY_RETURNED_DEVICE = "SELECT ko FROM KeeperOrder ko WHERE device_Id = :deviceId AND isReturned = true";

    @Query(FIND_KEEPER_ORDER_LIST_BY_DEVICE_ID)
    public List<KeeperOrder> findKeeperOrderByDeviceId(int deviceId);

    @Query(FIND_KEEPER_ORDER_BY_DEVICE_ID_AND_KEEPER_ID)
    public KeeperOrder findByDeviceIdAndKeeperId(int deviceId, int keeperId);

    @Query(FIND_KEEPER_ORDER_LIST_BY_KEEPER_ID)
    public List<KeeperOrder> findByKeeperId(int keeperId);

    @Query(FIND_KEEPER_ORDER_LIST_BY_RETURNED_DEVICE)
    public List<KeeperOrder> findByReturnedDevice(int deviceId);
}
