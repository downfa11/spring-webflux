package com.example.webflux1;

import com.example.webflux1.Controller.UserController;
import com.example.webflux1.Repository.User;
import com.example.webflux1.Service.UserService;
import com.example.webflux1.dto.UserCreateRequest;
import com.example.webflux1.dto.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@WebFluxTest(UserController.class)
@AutoConfigureWebTestClient
class UserControllerTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @Test
    void createUser() {
        when(userService.create("namseok", "namseok@pukyong.co.kr")).thenReturn(
                Mono.just(new User(1L, "namseok", "namseok@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.post().uri("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateRequest("namseok", "namseok@pukyong.co.kr"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("namseok", res.getName());
                    assertEquals("namseok@pukyong.co.kr", res.getEmail());
                });
    }

    @Test
    void findAllUsers() {
        when(userService.findAll()).thenReturn(
                Flux.just(
                        new User(1L, "namseok", "namseok@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now()),
                        new User(2L, "namseok", "namseok@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now()),
                        new User(3L, "namseok", "namseok@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now())
                ));

        webTestClient.get().uri("/users/memberList")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBodyList(UserResponse.class)
                .hasSize(3);
    }

    @Test
    void findUser() {
        when(userService.findById(1L)).thenReturn(
                Mono.just(new User(1L, "namseok", "namseok@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now())
                ));

        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("namseok", res.getName());
                    assertEquals("namseok@pukyong.co.kr", res.getEmail());
                });
    }

    @Test
    void notFoundUser() {
        when(userService.findById(1L)).thenReturn(Mono.empty());

        webTestClient.get().uri("/users/1")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void deleteUser() {
        when(userService.deleteById(1L)).thenReturn(Mono.empty());

        webTestClient.delete().uri("/users/1")
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    void updateUser() {
        when(userService.update(1L, "namseok1", "namseok1@pukyong.co.kr")).thenReturn(
                Mono.just(new User(1L, "namseok1", "namseok1@pukyong.co.kr", LocalDateTime.now(), LocalDateTime.now()))
        );

        webTestClient.put().uri("/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserCreateRequest("namseok1", "namseok1@pukyong.co.kr"))
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(UserResponse.class)
                .value(res -> {
                    assertEquals("namseok1", res.getName());
                    assertEquals("namseok1@pukyong.co.kr", res.getEmail());
                });
    }
}