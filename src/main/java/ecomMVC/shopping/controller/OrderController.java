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
import ecomMVC.shopping.dto.CheckoutResult;
import ecomMVC.shopping.dto.OrderItemDTO;
import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.entity.ProductEntity;
import ecomMVC.shopping.repository.ProductRepository;
import ecomMVC.shopping.service.ProductService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
// Khai báo: Bất cứ biến nào có tên này được đưa vào Model, hãy tự động ném nó
// vào Session
@SessionAttributes({ "selectedItems", "totalOrderPrice" })
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderController {

    @Autowired
    ProductService productService;

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

        // 1. Chốt chặn cơ bản nhất
        if (productIds == null || quantities == null || productIds.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng chọn ít nhất 1 sản phẩm!");
            return "redirect:/products";
        }
        // 2. Giao toàn bộ việc nặng nhọc cho Service
        CheckoutResult result = productService.processCheckout(productIds, quantities);

        // 3. Phân luồng điều hướng dựa trên kết quả
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorList", result.getErrorMessages());
            return "redirect:/products";
        }

        // 4. Nếu thành công, nhét vào Session (Model) và đi tiếp
        model.addAttribute("selectedItems", result.getSelectedItems());
        model.addAttribute("totalOrderPrice", result.getTotalOrderPrice());

        return "redirect:/order/summary";
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
