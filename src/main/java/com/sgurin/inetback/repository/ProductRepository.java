package com.sgurin.inetback.repository;

import com.sgurin.inetback.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
