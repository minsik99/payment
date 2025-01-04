package com.sparta.videoservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")
public interface UserClient {
    @PutMapping("/user-service/users/{userId}/role/creator")
    void updateRoleToCreator(@PathVariable Long userId);

    @GetMapping("/user-service/user-id")
    Long getUserIdByEmail(@RequestParam("email") String email);
}