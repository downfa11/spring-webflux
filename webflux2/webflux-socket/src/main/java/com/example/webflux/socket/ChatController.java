package com.example.webflux.socket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
public class ChatController {
    @GetMapping("/")
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("index").build());
    }
}
