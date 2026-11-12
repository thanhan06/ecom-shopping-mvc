package ecomMVC.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ecomMVC.shopping.constant.Constant;
import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.service.ProductService;
import ecomMVC.shopping.service.ProductTypeService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {
    @Autowired
    ProductService productService;

    @Autowired 
    ProductTypeService productTypeService;

    @GetMapping("/products")
    public String showProducts(@RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
        @RequestParam (value = "productName", required = false) String productName,
        @RequestParam (value = "productTypeId", required = false) String productTypeId,
        @RequestParam (value = "description", required = false) String description,
            Model model) {
        
        int pageSize = 5; // Số lượng sản phẩm trên mỗi trang
        Page<ProductResponse> productPage = productService.getActiveProductsWithFilters(productName, productTypeId, description, pageNo, pageSize);

        // Đẩy danh sách sản phẩm xuống view
        model.addAttribute("products", productPage.getContent());

        // Thông tin phân trang
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());
        
        // Đẩy các giá trị tìm kiếm xuống view để giữ nguyên khi phân trang
        model.addAttribute("productName", productName);
        model.addAttribute("productTypeId", productTypeId);
        model.addAttribute("description", description);
        
        // ==========================================
        // THÊM MỚI: Lấy danh sách Loại sản phẩm để vẽ thẻ <select>
        // (Giả sử bạn có hàm getAllActiveTypes trả về List các DTO)
        // ==========================================
        model.addAttribute("listProductTypes", productTypeService.getAllActiveTypes());
        return Constant.PRODUCTS;
    }

}
