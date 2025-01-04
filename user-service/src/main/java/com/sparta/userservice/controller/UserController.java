package com.sparta.userservice.controller;

import com.sparta.userservice.entity.User;
import com.sparta.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-service")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;

    // email로 userId를 조회
    @GetMapping("/user-id")
    public ResponseEntity<Long> getUserIdByEmail(@RequestParam("email") String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(user.getId());
    }

    // 역할 업데이트: USER -> CREATOR
    @PutMapping("/users/{userId}/role/creator")
    public ResponseEntity<Void> updateRoleToCreator(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 역할 업데이트
        user.setRole(User.Role.CREATOR);
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }
}