package ecomMVC.shopping.controller;

import org.hibernate.annotations.Audited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ecomMVC.shopping.dto.req.AuthenticationRequest;
import ecomMVC.shopping.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    @Autowired
    AuthenticationService authenticationService;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("authenticationReq", new AuthenticationRequest());
        return "login";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute("authenticationReq") AuthenticationRequest authenticationReq,
            Model model) {

        // Đảo ngược thứ tự so sánh chuỗi ("admin".equals) để tránh lỗi
        // NullPointerException nếu username bị null
        if (authenticationService.authenticate(authenticationReq)) {
            return "redirect:/";
        } else {
            // Đính kèm thông báo lỗi và quay lại đúng trang đăng nhập
            model.addAttribute("errorMessage", "Tên đăng nhập hoặc mật khẩu không chính xác!");
            return "login";
        }
    }
}
