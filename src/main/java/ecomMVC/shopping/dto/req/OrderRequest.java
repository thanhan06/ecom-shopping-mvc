package ecomMVC.shopping.dto.req;

import java.util.List;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data 
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequest {
    List<OrderItemRequest> orderItems;
}
