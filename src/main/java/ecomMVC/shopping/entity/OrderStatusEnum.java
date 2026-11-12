package ecomMVC.shopping.entity;

public enum OrderStatusEnum {
    PENDING, // Chờ xác nhận
    PROCESSING, // Đang xử lý / Đóng gói
    SHIPPED, // Đã giao cho đơn vị vận chuyển
    DELIVERED, // Đã giao hàng thành công
    CANCELLED // Đã hủy
}
