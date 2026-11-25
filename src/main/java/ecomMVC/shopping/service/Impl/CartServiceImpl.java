package ecomMVC.shopping.service.Impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import ecomMVC.shopping.service.CartService;
import jakarta.servlet.http.HttpSession;

@Service 
public class CartServiceImpl implements CartService {

    private static final String CART_SESSION_KEY = "cartQuantities";

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Integer> getCart(HttpSession httpSession) {
        Map<String, Integer> cart = (Map<String, Integer>) httpSession.getAttribute(CART_SESSION_KEY);

        if (cart == null) {
            cart = new HashMap<>();
            httpSession.setAttribute(CART_SESSION_KEY, cart);
        }

        return cart;
    }

    @Override
    public void updateCart(HttpSession httpSession, String productId, Integer quantity) {
        Map<String,Integer> cart= getCart(httpSession);
        if(quantity == null || quantity <0){
            cart.remove(productId);
        } else {
            cart.put(productId, quantity);
        }
        httpSession.setAttribute(CART_SESSION_KEY, cart);
    }

    @Override
    public void clearCart(HttpSession httpSession) {
        httpSession.removeAttribute(CART_SESSION_KEY);
    }

}
