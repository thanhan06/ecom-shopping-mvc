package ecomMVC.shopping.mapper;

import org.mapstruct.Mapper;

import ecomMVC.shopping.dto.res.ProductTypeResponse;
import ecomMVC.shopping.entity.ProductTypeEntity;

@Mapper(componentModel = "spring")
public interface ProductTypeMapper {
    public ProductTypeResponse toProductTypeResponse(ProductTypeEntity entity);
}
