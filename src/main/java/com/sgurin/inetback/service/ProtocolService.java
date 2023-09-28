package com.sgurin.inetback.service;

import com.sgurin.inetback.domain.Protocol;
import com.sgurin.inetback.repository.ProtocolRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProtocolService {
    private final ProtocolRepo protocolRepo;

    @Autowired
    public ProtocolService(ProtocolRepo protocolRepo) {
        this.protocolRepo = protocolRepo;
    }

    public Protocol save(Protocol protocol) {
        protocol.confirm();
        return protocolRepo.save(protocol);
    }

    public void clearOldData(int days){
        protocolRepo.clearOldData(days);
    }
}