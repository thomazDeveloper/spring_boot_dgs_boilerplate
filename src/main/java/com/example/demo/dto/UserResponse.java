package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private boolean success;
    private String message;
    private Optional<Mono<UserEntity>> data;
}
