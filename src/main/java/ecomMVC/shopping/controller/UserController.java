package ecomMVC.shopping.controller;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import ecomMVC.shopping.constant.Constant;
import ecomMVC.shopping.dto.req.AuthenticationRequest;
import ecomMVC.shopping.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@Controller
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserController {
    @Autowired
    AuthenticationService authenticationService;

    // Inject MessageSource để đọc file properties
    @Autowired
    MessageSource messageSource;

    @GetMapping
    public String index() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("authenticationReq", new AuthenticationRequest());
        return Constant.LOGIN;
    }

    @PostMapping("/login")
    public String loginSubmit(@Valid @ModelAttribute("authenticationReq") AuthenticationRequest authenticationReq,
            BindingResult bindingResult,
            Model model,
            Locale locale) { // Thêm tham số Locale để biết ngôn ngữ đang chọn

        // 1. Kiểm tra vi phạm Validation (trống, vượt quá ký tự...)
        if (bindingResult.hasErrors()) {
            // Trả ngay về trang login để Thymeleaf tự động render lỗi qua thẻ th:errors
            return Constant.LOGIN;
        }

        // 2. Nếu form điền hợp lệ, tiến hành xác thực tài khoản trong DB
        if (authenticationService.authenticate(authenticationReq)) {
            return Constant.REDIRECT + Constant.PRODUCTS;
        } else {
            // Đọc câu thông báo từ file properties tương ứng với ngôn ngữ đang chọn
            String errorMsg = messageSource.getMessage("login.error.invalid", null, locale);

            model.addAttribute("errorMessage", errorMsg);
            return Constant.LOGIN;
        }
    }
}
