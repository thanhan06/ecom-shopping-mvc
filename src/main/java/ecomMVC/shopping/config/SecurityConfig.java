package ecomMVC.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Cấp quyền truy cập tự do cho trang chủ, css, js, và form login
                .requestMatchers("/", "/css/**", "/js/**", "/login", "/register").permitAll()
                // Bắt buộc xác thực với mọi request khác (như /cart, /checkout, /profile)
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login") // Bỏ comment nếu bạn tự tạo trang login.html
                .defaultSuccessUrl("/products", true) // Đăng nhập thành công thì chuyển về trang chủ
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .permitAll()
            );
            
            // Xóa dòng csrf.disable() vì Form Login của Spring Security mặc định cần CSRF token để an toàn

        return http.build();
    }
}