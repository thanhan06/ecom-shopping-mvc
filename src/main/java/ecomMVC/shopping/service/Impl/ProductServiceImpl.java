package ecomMVC.shopping.service.Impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.CheckoutResult;
import ecomMVC.shopping.dto.OrderItemDTO;
import ecomMVC.shopping.dto.res.ProductResponse;
import ecomMVC.shopping.entity.ProductEntity;
import ecomMVC.shopping.mapper.ProductMapper;
import ecomMVC.shopping.repository.ProductRepository;
import ecomMVC.shopping.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor // Tự động tạo constructor tiêm dependency cho các biến final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động thêm private final
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAllWithProductType().stream()
                .map(productMapper::toProductResponse)
                .toList();
    }

    @Override
    public ProductResponse getProductById(String productId) {
        return productRepository.findByIdWithProductType(productId)
                .map(productMapper::toProductResponse)
                .orElse(null);
    }

    @Override
    public void validateProductAndQuantity(String productId, Integer quantity, CheckoutResult result) {
        // 1. Kiểm tra đầu vào
        if (quantity == null || quantity <= 0) {
            result.addError("Sản phẩm mã " + productId + " có số lượng không hợp lệ.");
            return; // Dùng return thay cho continue để thoát sớm khỏi hàm này
        }

        // 2. Truy vấn Database
        ProductResponse product = this.getProductById(productId);

        // 3. Xử lý tồn kho
        if (product == null) {
            result.addError("Sản phẩm mã " + productId + " không tồn tại.");
            return; // Thoát sớm
        }

        if (quantity <= product.productAmount()) {
            OrderItemDTO item = new OrderItemDTO(product, quantity);
            result.addItem(item); // Hợp lệ thì thêm vào giỏ
        } else {
            result.addError("Sản phẩm '" + product.productName() + "' vượt quá tồn kho (chỉ còn "
                    + product.productAmount() + ").");
        }
    }

    @Override
    public CheckoutResult processCheckout(List<String> productIds, List<Integer> quantities) {
        CheckoutResult result = new CheckoutResult();

        for (int i = 0; i < productIds.size(); i++) {
            // Gọi hàm phụ xử lý cho từng phần tử
            validateProductAndQuantity(productIds.get(i), quantities.get(i), result);
        }

        return result;
    }

    @Override
    public Page<ProductResponse> getActiveProductsWithFilters(
            String productName, String productTypeId, String description, int page, int size) {

        // CHỐT CHẶN AN TOÀN: Nếu user truyền số âm hoặc 0, tự động ép về trang 1
        int validPage = (page > 0) ? page : 1;
        Pageable pageable = PageRequest.of((validPage - 1), size);

        String searchName = (productName != null && !productName.isBlank())
                ? "%" + productName.trim().toLowerCase() + "%"
                : null;

        String searchDesc = (description != null && !description.isBlank())
                ? "%" + description.trim().toLowerCase() + "%"
                : null;

        String searchTypeId = (productTypeId != null && !productTypeId.isBlank()) ? productTypeId.trim() : null;

        Page<ProductEntity> productEntities = productRepository.findActiveProductsWithFilters(searchName, searchTypeId,
                searchDesc, pageable);

        return productEntities.map(productMapper::toProductResponse);
    }

}
