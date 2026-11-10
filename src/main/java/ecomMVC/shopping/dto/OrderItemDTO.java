package ecomMVC.shopping.dto;

import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.entity.ProductEntity;

public class OrderItemDTO {
    private ProductResponse product;
    private int quantity;

    public OrderItemDTO(ProductResponse product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getter và Setter
    public ProductResponse getProduct() { return product; }
    public void setProduct(ProductResponse product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    // Tính tổng tiền của 1 dòng
    public double getSubTotal() {
        return product.price() * quantity;
    }
}