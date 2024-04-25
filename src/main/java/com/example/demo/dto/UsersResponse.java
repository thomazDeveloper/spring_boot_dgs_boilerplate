package com.example.demo.dto;

import com.example.demo.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsersResponse {
    private boolean success;
    private String message;
    private Mono<List<UserEntity>> data;
}
