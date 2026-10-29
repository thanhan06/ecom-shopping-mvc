package ecomMVC.shopping.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ecomMVC.shopping.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String> {

        boolean existsByProductName(String productName);

        @Query(value = "SELECT MAX(p.product_id) FROM mstproduct p WHERE p.product_id LIKE :productTypeId || '%'", nativeQuery = true)
        String getMaxProductId(@Param("productTypeId") String productTypeId);

        @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productType")
        List<ProductEntity> findAllWithProductType();

        // // 2. Đã thêm các từ khóa AND, khoảng trắng và sửa lỗi chính tả description
        // @Query(
        // value = "SELECT p FROM ProductEntity p "
        // + "JOIN FETCH p.productType pt " // 1. BẮT BUỘC dùng JOIN FETCH ở đây và đặt
        // tên là pt
        // + "LEFT JOIN ProductOrderEntity o ON o.product = p "
        // + "WHERE "
        // + "(:productName IS NULL OR LOWER(p.productName) LIKE :productName) AND "
        // + "(:productTypeId IS NULL OR pt.productTypeId = :productTypeId) AND " // 2.
        // Gọi thẳng pt
        // + "(:description IS NULL OR LOWER(p.description) LIKE :description) AND "
        // + "p.status = true AND "
        // + "pt.status = true "
        // + "GROUP BY p, pt " // 3. ĐIỂM QUAN TRỌNG NHẤT: Bắt buộc Group By cả Entity p
        // và Entity pt
        // + "ORDER BY SUM(COALESCE(o.orderProductAmount, 0)) DESC, p.id DESC",
        // countQuery = "SELECT COUNT(p) FROM ProductEntity p "
        // + "JOIN p.productType pt " // Đếm thì không cần FETCH và không cần Order bảng
        // kia
        // + "WHERE "
        // + "(:productName IS NULL OR LOWER(p.productName) LIKE :productName) AND "
        // + "(:productTypeId IS NULL OR pt.productTypeId = :productTypeId) AND "
        // + "(:description IS NULL OR LOWER(p.description) LIKE :description) AND "
        // + "p.status = true AND "
        // + "pt.status = true")
        // Page<ProductEntity> findActiveProductsWithFilters(
        // @Param("productName") String productName,
        // @Param("productTypeId") String productTypeId,
        // @Param("description") String description,
        // Pageable pageable);

        // @Modifying
        // @Query("UPDATE ProductEntity p SET p.productAmount = p.productAmount -
        // :amount "
        // + "WHERE p.productId = :id AND p.productAmount >= :amount")
        // int deductStock(@Param("id") String id, @Param("amount") Integer amount);
}
