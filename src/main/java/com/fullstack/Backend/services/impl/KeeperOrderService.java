package com.fullstack.Backend.services.impl;

import com.fullstack.Backend.entities.KeeperOrder;
import com.fullstack.Backend.repositories.interfaces.IKeeperOrderRepository;
import com.fullstack.Backend.services.IKeeperOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
@Service
public class KeeperOrderService implements IKeeperOrderService {
    @Autowired
    IKeeperOrderRepository _keeperOrderRepository;

    @Override
    public CompletableFuture<List<KeeperOrder>> getKeeperOrderListByDeviceId(int deviceId) throws InterruptedException, ExecutionException {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findKeeperOrderByDeviceId(deviceId));
    }

    @Override
    public CompletableFuture<KeeperOrder> findKeeperOrderByDeviceIdAndKeeperId(int deviceId, int keeperId) throws InterruptedException, ExecutionException {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findByDeviceIdAndKeeperId(deviceId, keeperId));
    }

    @Override
    public void createKeeperOrder(KeeperOrder keeperOrder) throws InterruptedException, ExecutionException {
        _keeperOrderRepository.save(keeperOrder);
    }

    @Override
    public void updateKeeperOrder(KeeperOrder keeperOrder) throws InterruptedException, ExecutionException {
        _keeperOrderRepository.save(keeperOrder);
    }

    @Override
    public CompletableFuture<List<KeeperOrder>> getAllKeeperOrders() {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findAll());
    }
}
