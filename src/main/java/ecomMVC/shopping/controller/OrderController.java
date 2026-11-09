package ecomMVC.shopping.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecomMVC.shopping.constant.Constant;
import ecomMVC.shopping.dto.OrderItemDTO;
import ecomMVC.shopping.entity.ProductEntity;
import ecomMVC.shopping.repository.ProductRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
// Khai báo: Bất cứ biến nào có tên này được đưa vào Model, hãy tự động ném nó
// vào Session
@SessionAttributes({ "selectedItems", "totalOrderPrice" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderController {

    @Autowired
    ProductRepository productRepository;

    @GetMapping("/order/summary")
    public String showOrders(Model model) {
        // Logic để hiển thị danh sách đơn hàng của người dùng
        // Kiểm tra nếu người dùng truy cập thẳng URL này mà không qua bước chọn sản
        // phẩm
        if (!model.containsAttribute("selectedItems")) {
            return Constant.REDIRECT + Constant.PRODUCTS; // Chuyển hướng về trang danh sách sản phẩm nếu không có đơn
                                                          // hàng nào
        }
        return Constant.ORDERS; // Trả về tên view (orders.html)
    }

    @PostMapping("/order/checkout")
    public String processCheckout(
            @RequestParam(value = "productIds", required = false) List<String> productIds,
            @RequestParam(value = "quantities", required = false) List<Integer> quantities,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (productIds == null || quantities == null || productIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ít nhất 1 sản phẩm!");
            return "redirect:/products";
        }

        List<OrderItemDTO> selectedItems = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>(); // Khởi tạo mảng hứng lỗi
        double totalOrderPrice = 0;

        for (int i = 0; i < productIds.size(); i++) {
            String productId = productIds.get(i);
            Integer quantity = quantities.get(i);

            ProductEntity product = productRepository.findById(productId).orElse(null);

            if (product != null) {
                if (quantity <= product.getProductAmount()) {
                    OrderItemDTO item = new OrderItemDTO(product, quantity);
                    selectedItems.add(item);
                    totalOrderPrice += item.getSubTotal();
                } else {
                    // Thay vì return ngay, ta nhét câu thông báo vào danh sách lỗi
                    errorMessages.add("Sản phẩm '" + product.getProductName() + "' vượt quá tồn kho (chỉ còn "
                            + product.getProductAmount() + ").");
                }
            } else {
                errorMessages.add("Sản phẩm mã " + productId + " không tồn tại.");
            }
        }

        // SAU KHI VÒNG LẶP KẾT THÚC: Kiểm tra xem có lỗi nào không
        if (!errorMessages.isEmpty()) {
            // Gửi toàn bộ danh sách lỗi qua giao diện
            redirectAttributes.addFlashAttribute("errorList", errorMessages);
            return "redirect:/products"; // Chặn lại và quay về trang danh sách
        }

        // Nếu errorMessages rỗng (không có lỗi nào), tiến hành qua trang xác nhận
        model.addAttribute("selectedItems", selectedItems);
        model.addAttribute("totalOrderPrice", totalOrderPrice);

        return "redirect:/order/summary"; // Chuyển hướng sang trang xác nhận đơn hàng
    }

    // BƯỚC 3: Xử lý khi bấm "Xác nhận đặt hàng" ở trang order.html
    @PostMapping("/order/confirm")
    public String confirmOrder(
            @RequestParam("productIds") List<Long> productIds,
            @RequestParam("quantities") List<Integer> quantities,
            RedirectAttributes redirectAttributes) {

        // TODO: Viết logic lưu DB (Tạo Order, OrderDetail, trừ tồn kho) tại đây

        redirectAttributes.addFlashAttribute("successMessage", "Đặt hàng thành công!");
        return "redirect:/products";
    }

}
