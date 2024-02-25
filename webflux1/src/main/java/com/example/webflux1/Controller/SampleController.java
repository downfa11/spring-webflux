package com.example.webflux1.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class SampleController {

    @GetMapping("smaple/hello")
    public Mono<String> getHello(){
        // reactor : publisher <--> subscriber.
        // subscriber가 안보임. -> 사실 webflux가 subscribe함.
        return Mono.just("hello, rest controller with webflux");
    }
}
