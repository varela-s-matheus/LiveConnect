package com.LiveConnect.controller;

import com.LiveConnect.dto.UserLoginDTO;
import com.LiveConnect.model.User;
import com.LiveConnect.service.UserService;
import com.LiveConnect.service.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private TokenService tokenService;

    private UserController() {}

    @GetMapping("/{id}")
    public ResponseEntity<User> findUserById(@PathVariable int id) {
        try {
            User user  = userService.findUserById(id)
                    .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );
            return ResponseEntity.ok(user);

        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        try {
            return ResponseEntity.ok(userService.findAllUsers());
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable int id, @RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.update(id, user));
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> delete(@PathVariable int id) {
        try {
            if(userService.deleteById(id)) {
                return ResponseEntity.ok().build();
            }
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginDTO> login(@RequestBody User user) {
        try {
            if(!userService.existsByUsername(user.getUsername())) {
                userService.add(user);
            }
            var token = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            var authentication = manager.authenticate(token);

            return ResponseEntity.ok(
                    new UserLoginDTO(userService.findUserByUsername(
                            user.getUsername()).get().getId(),
                            tokenService.generateToken((User) authentication.getPrincipal())));

        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/token")
    public ResponseEntity<Boolean> validateToken(@RequestBody User user) {
        try {
            return ResponseEntity.ok(true);
        } catch(RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}
