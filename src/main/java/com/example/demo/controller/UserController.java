package com.example.demo.controller;

import com.example.demo.entity.UserEntity;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.r2dbc.core.R2dbcEntityOperations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final R2dbcEntityOperations operations;

    private final UserRepository userRepository;

    @GetMapping
    public Flux<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> findById(@PathVariable("id") final UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserEntity> create(@RequestBody final UserEntity entity) {
        entity.setId(UUID.randomUUID());
        return operations.insert(UserEntity.class)
                .using(entity)
                .then()
                .thenReturn(entity);
    }

    @PutMapping("/{id}")
    public Mono<UserEntity> update(@PathVariable("id") final UUID id, @RequestBody final UserEntity entity) {
        return findById(id)
                .flatMap(dbEntity -> {
                    dbEntity.setFirstName(entity.getFirstName());
                    dbEntity.setLastName(entity.getLastName());
                    return userRepository.save(dbEntity);
                });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") final UUID id) {
        return findById(id)
                .flatMap(dbEntity -> {
                    return userRepository.deleteById(dbEntity.getId());
                });
    }
}
