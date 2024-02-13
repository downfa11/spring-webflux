package com.example.webflux1.Controller;

import com.example.webflux1.Service.PostService;
import com.example.webflux1.dto.PostRequest;
import com.example.webflux1.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;


    @PostMapping("")
    public Mono<PostResponse> createPost(@RequestBody PostRequest request){
        return postService.create(request.getUserId(), request.getTitle(), request.getContent())
                .map(PostResponse::of);
    }

    @GetMapping("")
    public Flux<PostResponse> findAllPost(){
        return postService.findAll()
                .map(PostResponse::of);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> findPostById(@PathVariable Long id){
        return postService.findById(id)
                .map(u-> ResponseEntity.ok().body(PostResponse.of(u)))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));

    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<PostResponse>> deletePost(@PathVariable Long id){
        return postService.deleteById(id).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
