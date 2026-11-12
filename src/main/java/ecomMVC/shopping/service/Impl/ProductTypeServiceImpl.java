package ecomMVC.shopping.service.Impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.res.ProductTypeResponse;
import ecomMVC.shopping.mapper.ProductTypeMapper;
import ecomMVC.shopping.repository.ProductTypeRepository;
import ecomMVC.shopping.service.ProductTypeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductTypeServiceImpl implements ProductTypeService {
    @Autowired
    ProductTypeRepository productTypeRepository;
    @Autowired
    ProductTypeMapper productTypeMapper;

    @Override
    public List<ProductTypeResponse> getAllActiveTypes() {
        return productTypeRepository.findByStatusTrue().stream()
                .map(productTypeMapper::toProductTypeResponse)
                .toList();
    }

}
