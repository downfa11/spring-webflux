package com.example.webflux1.Service;

import com.example.webflux1.Repository.User;
import com.example.webflux1.Repository.UserR2dbcRepository;
import com.example.webflux1.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class UserService {

    //private final UserRepository;
    private final UserR2dbcRepository userRepository;
    private final ReactiveRedisTemplate<String, User> reactiveRedisTemplate;
    public Mono<User> create(String name, String email){
        return userRepository.save(User.builder().name(name).email(email).build());
    }

    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> findById(Long id){

        return reactiveRedisTemplate.opsForValue()
                .get("users:%d".formatted(id))
                .switchIfEmpty(userRepository.findById(id)
                        .flatMap(u-> reactiveRedisTemplate.opsForValue()
                                .set("users:%d".formatted(id),u, Duration.ofSeconds(30))
                                .then(Mono.just(u))));
        //return userRepository.findById(id);
    }

    public Mono<Void> deleteById(Long id){

        return userRepository.deleteById(id)
                .then(reactiveRedisTemplate.unlink("users:%d".formatted(id)))
                .then(Mono.empty());
    }
    public Mono<Void> deleteByName(String name) {
        return userRepository.deleteByName(name);}
    public Mono<User> update(Long id, String name, String email){
        return userRepository.findById(id)
                .flatMap(u -> {
                    u.setName(name);
                    u.setEmail(email);
                    return userRepository.save(u);
                })
                .flatMap(u->reactiveRedisTemplate.unlink("users:%d".formatted(id))
                        .then(Mono.just(u)));
                // map으로 하면 Mono<Mono<User>>를 반환
    }
}
