package com.event.service.service;

import com.event.service.domain.User;
import com.event.service.repository.UserServiceRepository;
import com.event.service.security.UserToUserDetail;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserInfoService implements UserDetailsService {
    public UserServiceRepository userServiceRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userServiceRepository.findById(username);
        return user.map(UserToUserDetail::new).orElseThrow(() -> new UsernameNotFoundException("User name not found " + username));

    }


}
