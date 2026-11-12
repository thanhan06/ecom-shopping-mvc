package ecomMVC.shopping.repository;

import java.util.List;
import java.util.Optional;

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

        @Query("SELECT p FROM ProductEntity p JOIN FETCH p.productType pt WHERE p.productId = :productId")
        Optional<ProductEntity> findByIdWithProductType(@Param("productId") String productId);

        // 2. Đã thêm các từ khóa AND, khoảng trắng và sửa lỗi chính tả description
        @Query(value = "SELECT p FROM ProductEntity p "
                        + "JOIN FETCH p.productType pt "
                        // ĐÃ XÓA LEFT JOIN BẢNG ORDER Ở ĐÂY
                        + "WHERE "
                        + "(:productName IS NULL OR LOWER(p.productName) LIKE :productName) AND "
                        + "(:productTypeId IS NULL OR pt.productTypeId = :productTypeId) AND "
                        + "(:description IS NULL OR LOWER(p.description) LIKE :description) AND "
                        + "p.status = true AND "
                        + "pt.status = true "
                        // ĐÃ XÓA LỆNH GROUP BY Ở ĐÂY

                        // THÊM SUBQUERY VÀO ORDER BY: Dùng để tính tổng bán ra mà không cần Join
                        + "ORDER BY (SELECT COALESCE(SUM(o.orderProductAmount), 0) FROM ProductOrderEntity o WHERE o.product = p) DESC, p.productId DESC",

                        countQuery = "SELECT COUNT(p) FROM ProductEntity p "
                                        + "JOIN p.productType pt "
                                        + "WHERE "
                                        + "(:productName IS NULL OR LOWER(p.productName) LIKE :productName) AND "
                                        + "(:productTypeId IS NULL OR pt.productTypeId = :productTypeId) AND "
                                        + "(:description IS NULL OR LOWER(p.description) LIKE :description) AND "
                                        + "p.status = true AND "
                                        + "pt.status = true")
        Page<ProductEntity> findActiveProductsWithFilters(
                        @Param("productName") String productName,
                        @Param("productTypeId") String productTypeId,
                        @Param("description") String description,
                        Pageable pageable);

        // @Modifying
        // @Query("UPDATE ProductEntity p SET p.productAmount = p.productAmount -
        // :amount "
        // + "WHERE p.productId = :id AND p.productAmount >= :amount")
        // int deductStock(@Param("id") String id, @Param("amount") Integer amount);
}
