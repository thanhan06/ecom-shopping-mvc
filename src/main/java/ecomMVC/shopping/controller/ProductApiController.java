package ecomMVC.shopping.controller;

import ecomMVC.shopping.entity.ProductEntity;
import ecomMVC.shopping.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ProductApiController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/api/check-stock")
    public ResponseEntity<Map<String,Object>> checkStock(
            @RequestParam String productId,
            @RequestParam int quantity) {

        Map<String, Object> response = new HashMap<>();

        // Bỏ qua kiểm tra nếu khách hàng reset về 0 (không mua)
        if (quantity <= 0) {
            response.put("valid", true);
            return ResponseEntity.ok(response);
        }

        ProductEntity product = productRepository.findById(productId).orElse(null);
        if (product == null) {
            response.put("valid", false);
            response.put("message", "Sản phẩm không tồn tại!");
            return ResponseEntity.ok(response);
        }

        if (quantity > product.getProductAmount()) {
            response.put("valid", false);
            response.put("message","Sản phẩm "+ product.getProductName() + " chỉ còn " + product.getProductAmount() + " chiếc");
            response.put("actualStock", product.getProductAmount());
        } else {
            response.put("valid", true);
            response.put("message", "");
        }

        return ResponseEntity.ok(response);
    }
}