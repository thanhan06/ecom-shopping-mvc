package ecomMVC.shopping.service.Impl;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.req.AuthenticationRequest;
import ecomMVC.shopping.entity.UserEntity;
import ecomMVC.shopping.repository.UserRepository;
import ecomMVC.shopping.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor // Tự động tạo constructor tiêm dependency cho các biến final
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true) // Tự động thêm private final
public class AuthenticationServiceImpl implements AuthenticationService {

    UserRepository userRepository;
    PasswordEncoder passwordEncoder; // Khai báo thêm PasswordEncoder

    @Override
    public boolean authenticate(AuthenticationRequest authenticationRequest) {
        Optional<UserEntity> userOptional = userRepository.findByUserId(authenticationRequest.getUsername());

        // Nếu không tìm thấy user trong Database, lập tức trả về false
        if (userOptional.isEmpty()) {
            return false;
        }

        // Lấy đối tượng user ra an toàn vì đã qua bước kiểm tra
        UserEntity user = userOptional.get();

        // So sánh mật khẩu
        return passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword());
    }
}