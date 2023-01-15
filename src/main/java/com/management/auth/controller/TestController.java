package com.management.auth.controller;

import com.management.auth.entity.Role;
import com.management.auth.entity.User;
import com.management.auth.repository.RoleRepository;
import com.management.auth.repository.UserRepository;
import com.management.auth.service.impl.UserServiceImpl;
import com.management.jwt.*;
import com.management.jwt.refreshToken.RefreshToken;
import com.management.jwt.refreshToken.RefreshTokenService;
import com.management.jwt.refreshToken.exception.TokenRefreshException;
import com.management.jwt.request.LoginRequest;
import com.management.jwt.request.SignupRequest;
import com.management.jwt.request.TokenRefreshRequest;
import com.management.jwt.response.JwtResponse;
import com.management.jwt.response.MessageResponse;
import com.management.jwt.response.TokenRefreshResponse;
import com.management.jwt.service.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/admin")
public class TestController {
    private static final Logger logger = LoggerFactory.getLogger(TestController.class);
    private final UserServiceImpl userService;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public TestController(UserServiceImpl userService,
                          JwtUtils jwtUtils,
                          AuthenticationManager authenticationManager,
                          RefreshTokenService refreshTokenService,
                          PasswordEncoder encoder,
                          UserRepository userRepository,
                          RoleRepository roleRepository) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
        this.encoder = encoder;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/sessionId")
    public ResponseEntity<?> sessionId() {
        return ResponseEntity.ok().body(RequestContextHolder.currentRequestAttributes().getSessionId());
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, refreshToken.getToken(), userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();

        return refreshTokenService.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateTokenFromUsername(user.getUsername());
                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
                        "Refresh token is not in database!"));
    }

    @PostMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        System.out.println(request.getSession().getId());
        request.getSession().invalidate();
        System.out.println(request.getSession().getId());
        return "redirect:/";
    }
}