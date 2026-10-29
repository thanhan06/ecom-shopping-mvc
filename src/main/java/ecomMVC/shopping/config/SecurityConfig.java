package ecomMVC.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Class SecurityConfig to config Spring Security
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Cho phép truy cập tự do vào tất cả các link (để UserController tự quản lý)
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                // Tạm tắt CSRF để form POST submit lên /login không bị lỗi 403 Forbidden
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
