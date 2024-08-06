package com.example.springjwt.controller;

import com.example.springjwt.configuration.JwtTokenUtil;
import com.example.springjwt.entity.LogUser;
import com.example.springjwt.model.JwtRequest;
import com.example.springjwt.repository.LogUserRepository;
import com.example.springjwt.service.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class JwtAuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private LogUserRepository logUserRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("create-user")
    public ResponseEntity<?> createUser(@RequestBody JwtRequest jwtRequest) {
        LogUser logUser = new LogUser();
        logUser.setUsername(jwtRequest.getUsername());
        logUser.setPassword(passwordEncoder.encode(jwtRequest.getPassword()));
        logUser = logUserRepository.save(logUser);

        return ResponseEntity.ok(logUser);
    }

    @GetMapping("hello")
    public ResponseEntity<?> firstPage() {
        try {

            return ResponseEntity.ok("Hello World");

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Something went wrong");
        }
    }

    @PostMapping("authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        try {

            LogUser logUser = logUserRepository.findByUsername(authenticationRequest.getUsername()).orElse(null);
            if (logUser == null) {
                return ResponseEntity.ok("user is null");
            }
            if (!passwordEncoder.matches(authenticationRequest.getPassword(), logUser.getPassword())) {
                return ResponseEntity.ok("password does not match");
            }

            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(authenticationRequest.getUsername());

            final String token = jwtTokenUtil.generateToken(userDetails);
            logUser.setToken(token);
            logUser = logUserRepository.save(logUser);

            return ResponseEntity.ok(logUser);

        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
