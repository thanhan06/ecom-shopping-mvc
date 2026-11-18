package ecomMVC.shopping.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.CheckoutResult;
import ecomMVC.shopping.dto.req.OrderItemRequest;
import ecomMVC.shopping.dto.res.ProductResponse;

@Service
public interface ProductService {
    public List<ProductResponse> getAllProducts();

    public ProductResponse getProductById(String productId);

    public void validateProductAndQuantity(OrderItemRequest orderItemRequest, CheckoutResult result);

    public CheckoutResult processCheckout(List<OrderItemRequest> orderItems);

    public Page<ProductResponse> getActiveProductsWithFilters(
            String productName, String productTypeId, String description, int page, int size);

}
