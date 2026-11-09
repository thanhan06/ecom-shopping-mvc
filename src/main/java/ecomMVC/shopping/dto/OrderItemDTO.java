package ecomMVC.shopping.dto;

import ecomMVC.shopping.entity.ProductEntity;

public class OrderItemDTO {
    private ProductEntity product;
    private int quantity;

    public OrderItemDTO(ProductEntity product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // Getter và Setter
    public ProductEntity getProduct() { return product; }
    public void setProduct(ProductEntity product) { this.product = product; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    
    // Tính tổng tiền của 1 dòng
    public double getSubTotal() {
        return product.getPrice() * quantity;
    }
}