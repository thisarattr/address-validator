package org.thisarattr.auspost.addressvalidator.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public LoginController(UserService userService, BCryptPasswordEncoder bcryptPasswordEncoder,
                           CacheService cacheService) {
        this.userService = userService;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
        this.cacheService = cacheService;
    }

    /**
     * Login will validate credentials and if success return the jwt token for subsequent api calls.
     * @param loginRequest request object with username and password
     * @return 200 if login success, 400 if mandatory fields are not provided
     *          401 if user not found in the database or incorrect password.
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {

        if (loginRequest.getUsername() == null || loginRequest.getPassword() == null) {
            log.debug("login failed, username or password missing");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new LoginResponse(false, 400, "Username and password are mandatory"));
        }

        Optional<User> user = userService.find(loginRequest.getUsername());
        if (!user.isPresent()) {
            log.debug("login failed, user not found for the user: " + loginRequest.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new LoginResponse(false, 401, "User not found"));
        }

        if (!bcryptPasswordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            log.debug("login failed, incorrect password for the user: " + loginRequest.getUsername());
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
