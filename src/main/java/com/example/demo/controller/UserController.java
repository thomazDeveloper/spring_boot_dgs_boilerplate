package com.example.demo.controller;

import com.example.demo.dto.UserResponse;
import com.example.demo.entity.UserEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@DgsComponent
@RequiredArgsConstructor
public class UserController {

    private final R2dbcEntityOperations operations;

    private final UserRepository userRepository;

    @DgsQuery
    public Mono<List<UserEntity>> findAll() {
        return userRepository.findAll().collect(Collectors.toList());
    }

    @DgsQuery
    public UserResponse findById(@InputArgument String id) {
        UUID uuid = UUID.fromString(id);
        Mono<UserEntity> user = userRepository.findById(uuid)
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
        return new UserResponse(true, "success", Optional.of(user));
    }

    public UserResponse create(@RequestBody final UserEntity entity) {
        entity.setUserId(UUID.randomUUID());
        Mono<UserEntity> user = operations.insert(UserEntity.class)
                .using(entity)
                .then()
                .thenReturn(entity);
        return new UserResponse(true, "success", Optional.of(user));
    }

    public UserResponse update(@PathVariable("id") final String id, @RequestBody final UserEntity entity) {
        UUID uuid = UUID.fromString(id);
        Mono<UserEntity> user =  userRepository.findById(uuid)
                .flatMap(dbEntity -> {
                    dbEntity.setFirstName(entity.getFirstName());
                    dbEntity.setLastName(entity.getLastName());
                    return userRepository.save(dbEntity);
                });

        return new UserResponse(true, "success", Optional.of(user));
    }

    public UserResponse deleteById(@PathVariable("id") final String id) {
        UUID uuid = UUID.fromString(id);
        userRepository.findById(uuid)
                .flatMap(dbEntity -> {
                 return userRepository.deleteById(dbEntity.getUserId());
                });
        return new UserResponse(true, "success", null);
    }
}
