package ecomMVC.shopping.service.Impl;

import ecomMVC.shopping.entity.UserEntity;
import ecomMVC.shopping.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.User;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Tìm user trong database
        UserEntity user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy tài khoản: " + username));

        // 2. Chuyển đổi Entity User của bạn thành đối tượng UserDetails của Spring
        // Security
        // Lưu ý: user.getPassword() phải là mật khẩu đã được mã hóa (ví dụ: BCrypt)
        return User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                // Gán quyền, ví dụ user.getRole() trả về "USER" hoặc "ADMIN"
                .roles(user.getRole())
                .build();
    }
}