package com.sgurin.inetback.repository;

import com.sgurin.inetback.domain.Protocol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface ProtocolRepo extends JpaRepository<Protocol, Long> {

    @Modifying
    @Transactional
    @Query(nativeQuery = true,value = "delete from inet_back.protocol where created_at <= current_date - :days * INTERVAL '1 DAY'")
    void clearOldData(@Param("days") int days);
}