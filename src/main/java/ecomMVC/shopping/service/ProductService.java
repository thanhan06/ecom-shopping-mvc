package ecomMVC.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.res.ProductResponse;

@Service
public interface ProductService {
    public List<ProductResponse> getAllProducts();
}
