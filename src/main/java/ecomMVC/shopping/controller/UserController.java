package ecomMVC.shopping.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ecomMVC.shopping.constant.Constant;
import ecomMVC.shopping.dto.req.AuthenticationRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {

    @Autowired
    MessageSource messageSource;

    @GetMapping
    public String index() {
        return "index";
    }

    // Spring Security sẽ tự động gọi GET /login?error nếu đăng nhập sai
    @GetMapping("/login")
    public String login(
            @RequestParam(value = "error", required = false) String error, 
            @RequestParam(value = "logout", required = false) String logout,
            Model model, 
            Locale locale) {
        
        // Bắt lỗi từ Spring Security để hiển thị thông báo i18n
        if (error != null) {
            String errorMsg = messageSource.getMessage("login.error.invalid", null, locale);
            model.addAttribute("errorMessage", errorMsg);
        }
        
        if (logout != null) {
            model.addAttribute("successMessage", "Bạn đã đăng xuất thành công.");
        }

        model.addAttribute("authenticationReq", new AuthenticationRequest());
        return Constant.LOGIN;
    }

    // XÓA HOÀN TOÀN @PostMapping("/login")
}