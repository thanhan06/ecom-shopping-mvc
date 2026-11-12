package ecomMVC.shopping.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "mstproductorder")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductOrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "customer_name", nullable = false, length = 200)
    String customerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_product_id", nullable = false)
    ProductEntity product;

    @Column(name = "order_product_amount", nullable = false)
    Integer orderProductAmount;

    @Column(name = "unit_price", nullable = false)
    Long unitPrice;

    @Column(name = "total_price", nullable = false)
    Long totalPrice;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 50)
    OrderStatusEnum orderStatus = OrderStatusEnum.PENDING;

    @Column(name = "order_delivery_address", nullable = false, length = 400)
    String orderDeliveryAddress;

    @Column(name = "order_delivery_date", nullable = false)
    LocalDate orderDeliveryDate;

    @Column(name = "create_time", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createTime;

    @Column(name = "create_user", length = 8, updatable = false)
    String createUser;

    @Column(name = "update_time")
    @UpdateTimestamp
    LocalDateTime updateTime;

    @Column(name = "update_user", length = 8)
    String updateUser;
}
