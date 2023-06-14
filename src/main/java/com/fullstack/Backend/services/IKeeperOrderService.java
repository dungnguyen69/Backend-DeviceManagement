package com.fullstack.Backend.services;

import com.fullstack.Backend.dto.keeper_order.KeeperOrderAddDTO;
import com.fullstack.Backend.entities.KeeperOrder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface IKeeperOrderService {
    public CompletableFuture<List<KeeperOrder>> getKeeperOrderListByDeviceId(int deviceId)
            throws InterruptedException, ExecutionException;
    public CompletableFuture<KeeperOrder> findKeeperOrderByDeviceIdAndKeeperId(int deviceId,int keeperId)
            throws InterruptedException, ExecutionException;

    public void createKeeperOrder(KeeperOrder keeperOrder)
            throws InterruptedException, ExecutionException;

    public void updateKeeperOrder(KeeperOrder keeperOrder)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<List<KeeperOrder>> getAllKeeperOrders();

    public CompletableFuture<List<KeeperOrder>> findByKeeperId(int keeperId);

}
