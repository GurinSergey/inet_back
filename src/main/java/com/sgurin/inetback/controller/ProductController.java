package com.sgurin.inetback.controller;

import com.sgurin.inetback.annotation.aspect.LogMethod;
import com.sgurin.inetback.domain.Product;
import com.sgurin.inetback.domain.User;
import com.sgurin.inetback.repository.ProductRepository;
import com.sgurin.inetback.response.GenericResponse;
import com.sgurin.inetback.service.TokenAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final TokenAuthService tokenAuthService;
    private final ProductRepository productRepository;

    @Autowired
    public ProductController(TokenAuthService tokenAuthService, ProductRepository productRepository) {
        this.tokenAuthService = tokenAuthService;
        this.productRepository = productRepository;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    //@RolesAllowed("ROLE_ADMIN")
    @LogMethod(logName = "createProduct", needSuccessLog = true)
    public ResponseEntity<GenericResponse<Product>> create(@RequestBody @Valid Product product) {
        Product savedProduct = productRepository.save(product);

        return GenericResponse.success(savedProduct);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    //@RolesAllowed({"ROLE_ADMIN","ROLE_USER"})
    @LogMethod(logName = "getProductList", needSuccessLog = true)
    public ResponseEntity<GenericResponse<List<Product>>> list() {
        User currentUser = tokenAuthService.getCurrentUser();

        return GenericResponse.success(productRepository.findAll());
    }
}
