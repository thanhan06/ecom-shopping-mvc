package ecomMVC.shopping.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Spring tự động chạy hàm này trước khi load bất kỳ trang web nào để đính kèm
    // thông tin URL hiện tại vào Model, giúp Thymeleaf có thể truy cập và sử dụng
    @ModelAttribute("currentUrl")
    public String populateCurrentUrl(HttpServletRequest request) {
        return request.getRequestURI();
    }
}