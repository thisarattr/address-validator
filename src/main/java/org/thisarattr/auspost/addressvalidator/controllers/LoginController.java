package org.thisarattr.auspost.addressvalidator.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.thisarattr.auspost.addressvalidator.dtos.LoginRequest;
import org.thisarattr.auspost.addressvalidator.dtos.LoginResponse;
import org.thisarattr.auspost.addressvalidator.models.User;
import org.thisarattr.auspost.addressvalidator.services.CacheService;
import org.thisarattr.auspost.addressvalidator.services.UserService;

@RestController
public class LoginController {

    private final UserService userService;
    private final BCryptPasswordEncoder bcryptPasswordEncoder;
    private final CacheService cacheService;
    @Value("${security.jwt.secret}")
    private String secretKey;

    @Autowired
    public LoginController(UserService userService, BCryptPasswordEncoder bcryptPasswordEncoder,
                           CacheService cacheService) {
        this.userService = userService;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
        this.cacheService = cacheService;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, 400, "Username and password are mandatory"));
        }

        Optional<User> user = userService.find(loginRequest.getUsername());
        if (!user.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, 401, "User not found"));
        }

        if (!bcryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, 401, "Incorrect username or password"));
        }

        String jwtToken =  Jwts.builder()
                                .setSubject(loginRequest.getUsername())
                                .claim("role", user.get().getRole())
                                .setIssuedAt(new Date())
                                .signWith(SignatureAlgorithm.HS256, secretKey)
                                .compact();
        cacheService.put(jwtToken, user.get());

        return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(true, jwtToken));
    }
}
