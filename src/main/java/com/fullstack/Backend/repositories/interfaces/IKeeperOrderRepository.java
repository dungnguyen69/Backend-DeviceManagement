package com.fullstack.Backend.repositories.interfaces;

import com.fullstack.Backend.entities.KeeperOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IKeeperOrderRepository extends JpaRepository<KeeperOrder, Long> {
    public static final String FIND_KEEPER_ORDER_LIST_BY_DEVICE_ID = "SELECT * FROM keeper_order WHERE device_id = :deviceId AND is_returned = false";
    public static final String FIND_KEEPER_ORDER_BY_DEVICE_ID_AND_KEEPER_ID = "SELECT * FROM keeper_order WHERE device_id = :deviceId AND keeper_id = :keeperId AND is_returned = false";

    @Query(value = FIND_KEEPER_ORDER_LIST_BY_DEVICE_ID, nativeQuery = true)
    public List<KeeperOrder> findKeeperOrderByDeviceId(int deviceId);

    @Query(value = FIND_KEEPER_ORDER_BY_DEVICE_ID_AND_KEEPER_ID, nativeQuery = true)
    public KeeperOrder findByDeviceIdAndKeeperId(int deviceId, int keeperId);
}
