package ecomMVC.shopping.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.res.ProductTypeResponse;

@Service 
public interface ProductTypeService {
    public List<ProductTypeResponse> getAllActiveTypes();
}
