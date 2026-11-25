package ecomMVC.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ecomMVC.shopping.service.CartService;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@RestController
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartController {
    @Autowired 
    CartService cartService;

    @PostMapping("/cart/update")
    public void updateCart(
        @RequestParam String productId,
        @RequestParam Integer quantity,
        HttpSession session
    ){
        cartService.updateCart(session, productId, quantity);
    }
}
