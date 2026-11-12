package ecomMVC.shopping.dto;

import java.util.ArrayList;
import java.util.List;

public class CheckoutResult {
    private List<String> errorMessages = new ArrayList<>();
    private List<OrderItemDTO> selectedItems = new ArrayList<>();
    private double totalOrderPrice = 0;

    // Getter và Setter
    public List<String> getErrorMessages() { return errorMessages; }
    public List<OrderItemDTO> getSelectedItems() { return selectedItems; }
    public double getTotalOrderPrice() { return totalOrderPrice; }
    
    public void addError(String error) { this.errorMessages.add(error); }
    public void addItem(OrderItemDTO item) { 
        this.selectedItems.add(item); 
        this.totalOrderPrice += item.getSubTotal();
    }
    
    // Hàm kiểm tra xem có hợp lệ không
    public boolean hasErrors() {
        return !errorMessages.isEmpty();
    }
}