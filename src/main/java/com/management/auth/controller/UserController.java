package com.management.auth.controller;

import com.management.auth.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/admin/user")
public class UserController {

    public final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/get/{userId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUserById(userId));
    }

    @GetMapping("/getUserByUsername")
    public ResponseEntity<?> getUserByUsername(@RequestParam String username) {
        return ResponseEntity.ok().body(userService.getUserByUsername(username));
    }

    @GetMapping("/getUserTasks/{userId}")
    public ResponseEntity<?> getUserTasks(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(userService.getUserTasks(userId));
    }
}