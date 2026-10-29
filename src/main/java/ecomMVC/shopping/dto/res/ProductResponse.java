package ecomMVC.shopping.dto.res;

public record ProductResponse(
        String productId,
        String productName,
        boolean status,
        String description,
        String productImg,
        Integer productAmount,
        Long price,
        String productTypeId,
        String productTypeName) {}
