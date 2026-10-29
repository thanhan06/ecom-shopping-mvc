package ecomMVC.shopping.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "productType.productTypeId", target = "productTypeId")
    @Mapping (source = "productType.productTypeName", target = "productTypeName")
    public ProductResponse toProductResponse(ProductEntity entity);

}
