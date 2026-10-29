package ecomMVC.shopping.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {
    
    @NotBlank(message = "{login.validation.username.notblank}")
    @Size(max = 20, message = "{login.validation.username.size}")
    String username;

    @NotBlank(message = "{login.validation.password.notblank}")
    @Size(min = 5, message = "{login.validation.password.size}")
    String password;
}