package com.event.service.controller;

import com.event.service.dto.AuthRequestDto;
import com.event.service.security.JwtService;
import com.event.service.util.EndPoint;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TokenController {



    private JwtService jwtUtil;


    private AuthenticationManager authenticationManager;

    @PostMapping(value = EndPoint.TOKEN, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getToken(@RequestBody AuthRequestDto authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getId(), authRequest.getPassword()));

        if(authenticate.isAuthenticated()){
            return jwtUtil.generateToken(authRequest.getId());
        }else {
            throw new UsernameNotFoundException("User "+authRequest.getId()+" not found");
        }

    }
}
