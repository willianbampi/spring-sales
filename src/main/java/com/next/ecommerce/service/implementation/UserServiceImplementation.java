package com.next.ecommerce.service.implementation;

import java.util.Optional;

import com.next.ecommerce.domain.repository.UserRepository;
import com.next.ecommerce.exception.PassswordInvalidException;
import com.next.ecommerce.domain.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImplementation implements UserDetailsService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                                                    .orElseThrow(() -> UsernameNotFoundException("User not found."));
        String[] roles = user.getAdmin() ? new String[]{"USER", "ADMIN"} : new String[]{"USER"};
        return User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles(roles)
                    .build();
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public UserDetails authentication(User user) {
        UserDetails userDetails = loadUserByUsername(user.getUsername());
        boolean isSamePassword = passwordEncoder.matches(user.getPassword(), userDetails.getPassword());
        if(isSamePassword) {
            return userDetails;
        }
        throw new PassswordInvalidException("Invalid Username or Password.");
    }
    
}
