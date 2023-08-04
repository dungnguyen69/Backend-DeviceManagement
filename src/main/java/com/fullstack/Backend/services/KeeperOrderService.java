package com.fullstack.Backend.services;

import com.fullstack.Backend.entities.KeeperOrder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface KeeperOrderService {
    public CompletableFuture<List<KeeperOrder>> getListByDeviceId(int deviceId)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<KeeperOrder> findByDeviceIdAndKeeperId(int deviceId,int keeperId)
            throws InterruptedException, ExecutionException;

    public void create(KeeperOrder keeperOrder)
            throws InterruptedException, ExecutionException;

    public void update(KeeperOrder keeperOrder)
            throws InterruptedException, ExecutionException;

    public CompletableFuture<List<KeeperOrder>> getAllKeeperOrders();

    public CompletableFuture<List<KeeperOrder>> findByKeeperId(int keeperId);

    public void findByReturnedDevice(int deviceId);
}
