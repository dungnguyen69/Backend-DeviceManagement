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
    public CompletableFuture<List<KeeperOrder>> getListByDeviceId(int deviceId) throws InterruptedException, ExecutionException {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findKeeperOrderByDeviceId(deviceId));
    }

    @Override
    public CompletableFuture<KeeperOrder> findByDeviceIdAndKeeperId(int deviceId, int keeperId) throws InterruptedException, ExecutionException {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findByDeviceIdAndKeeperId(deviceId, keeperId));
    }

    @Override
    public void create(KeeperOrder keeperOrder) throws InterruptedException, ExecutionException {
        _keeperOrderRepository.save(keeperOrder);
    }

    @Override
    public void update(KeeperOrder keeperOrder) {
        _keeperOrderRepository.save(keeperOrder);
    }

    @Override
    public CompletableFuture<List<KeeperOrder>> getAllKeeperOrders() {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findAll());
    }

    @Override
    public CompletableFuture<List<KeeperOrder>> findByKeeperId(int keeperId) {
        return CompletableFuture.completedFuture(_keeperOrderRepository.findByKeeperId(keeperId));
    }

    @Override
    public void findByReturnedDevice(int deviceId) {
        List<KeeperOrder> keeperOrderList = _keeperOrderRepository.findByReturnedDevice(deviceId);
        for(KeeperOrder keeperOrder: keeperOrderList){
            _keeperOrderRepository.delete(keeperOrder);
        }

    }
}
