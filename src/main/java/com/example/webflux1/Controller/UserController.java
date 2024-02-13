package com.example.webflux1.Controller;

import com.example.webflux1.Service.PostService;
import com.example.webflux1.Service.UserService;
import com.example.webflux1.dto.UserCreateRequest;
import com.example.webflux1.dto.UserPostResponse;
import com.example.webflux1.dto.UserResponse;
import com.example.webflux1.dto.UserUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final PostService postService;

    @PostMapping("/register")
    public Mono<UserResponse> createUser(@RequestBody UserCreateRequest request){
        return userService.create(request.getName(), request.getEmail())
                        .map(UserResponse::of);
    }
    @GetMapping("/memberList")
    public Flux<UserResponse> findAllUsers(){
        return userService.findAll()
                .map(UserResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<UserResponse>> findUser(@PathVariable Long id){
        return userService.findById(id)
                .map(u -> ResponseEntity.ok(UserResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<?>> deleteUser(@PathVariable Long id){
        return userService.deleteById(id).then(
                Mono.just(ResponseEntity.noContent().build()));
    }
    @DeleteMapping("/delete")
    public Mono<ResponseEntity<?>> deleteUserByName(@RequestParam String name){
        return userService.deleteByName(name).then(Mono.just(ResponseEntity.noContent().build()));
    }
    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<UserResponse>> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request){

        return userService.update(id, request.getName(), request.getEmail())
                .map(u -> ResponseEntity.ok(UserResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/{id}/posts")
    public Flux<UserPostResponse> getUserPosts(@PathVariable Long id){
        return postService.findAllByuserId(id)
                .map(UserPostResponse::of);
    }
}
