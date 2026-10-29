package ecomMVC.shopping.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.mapper.ProductMapper;
import ecomMVC.shopping.repository.ProductRepository;
import ecomMVC.shopping.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor // Tự động tạo constructor tiêm dependency cho các biến final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động thêm private final
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override 
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllWithProductType().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }
}
