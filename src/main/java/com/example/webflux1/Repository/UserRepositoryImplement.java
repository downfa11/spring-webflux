package com.example.webflux1.Repository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
@Repository
public class UserRepositoryImplement implements UserRepository{

    private final ConcurrentHashMap<Long,User> userHashmap = new ConcurrentHashMap<>();
    private AtomicLong sequence = new AtomicLong(1L);
    @Override
    public Mono<User> save(User user) {
        var now = LocalDateTime.now();

        if(user.getId()==null){
            user.setId(sequence.getAndAdd(1)); //1부터 유저들올떄마다 1씩 더한 값을 id로 받음
            user.setCreatedAt(now);
        }
        user.setUpdatedAt(now);
        userHashmap.put(user.getId(),user);
        return Mono.just(user);
    }

    @Override
    public Flux<User> findAll() {
        return Flux.fromIterable(userHashmap.values());
    }

    @Override
    public Mono<User> findById(Long id) {
        return Mono.justOrEmpty(userHashmap.getOrDefault(id,null));
    }

    @Override
    public Mono<Integer> deleteById(Long id) {
        User user = userHashmap.getOrDefault(id,null);
        if(user==null)
            return Mono.just(0);

        userHashmap.remove(id,user);
        return Mono.just(1);
    }
}
