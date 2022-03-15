package com.next.ecommerce.rest.controller;

import javax.validation.Valid;

import com.next.ecommerce.domain.entity.User;
import com.next.ecommerce.exception.PassswordInvalidException;
import com.next.ecommerce.rest.dto.UserRequestDTO;
import com.next.ecommerce.rest.dto.UserResponseDTO;
import com.next.ecommerce.rest.dto.UserTokenResponseDTO;
import com.next.ecommerce.security.JWTService;
import com.next.ecommerce.service.implementation.UserServiceImplementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/user")
@Api("User API")
public class UserController {

    @Autowired
    private UserServiceImplementation userServiceImplementation;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Save a new user")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Success."),
        @ApiResponse(responseCode = "400", description = "Validation error(s).")
    })
    public UserResponseDTO save(@RequestBody @Valid @ApiParam("USer") User user) {
        String endocedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(endocedPassword);
        User savedUser = userServiceImplementation.save(user);
        return new UserResponseDTO(savedUser.getId(), savedUser.getUsername());
    }

    @PostMapping("/auth")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation("Do the authentication")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success."),
        @ApiResponse(responseCode = "401", description = "Unauthorized.")
    })
    public UserTokenResponseDTO authentication(@RequestBody @ApiParam("User") UserRequestDTO userRequest) {
        try {
            User user = User.builder()
                            .username(userRequest.getUsername())
                            .password(userRequest.getPassword())
                            .build();
            UserDetails userDetails = userServiceImplementation.authentication(user);
            String token = jwtService.tokenGenerate(user);
            return new UserTokenResponseDTO(user.getUsername(), token);
        } catch (UsernameNotFoundException | PassswordInvalidException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }
    
}
