package ecomMVC.shopping.service;

import org.springframework.stereotype.Service;

import ecomMVC.shopping.dto.req.AuthenticationRequest;

@Service 
public interface AuthenticationService {
    boolean authenticate(AuthenticationRequest authenticationRequest);
}
