package ecomMVC.shopping.service;

import java.util.Map;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public interface CartService {
    public Map<String, Integer> getCart(HttpSession httpSession);

    public void updateCart(HttpSession httpSession, String productId, Integer quantity);

    public void clearCart(HttpSession httpSession);
}
