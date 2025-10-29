package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginRequest;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginResponse;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserUpdateDto;
import dev.matheuslf.desafio.inscritos.services.LoginService;
import dev.matheuslf.desafio.inscritos.services.UserService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hibernate.annotations.DiscriminatorFormula;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private LoginService loginService;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;
    private String fakeTokenValue;
    private User user;
    private UserCreateDto userCreate;
    private UserResponseDto userResponse;
    private UserUpdateDto userUpdate;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);

        UUID uuid = UUID.randomUUID();
        JwtClaimsSet fakeClaims = JwtClaimsSet.builder()
                .issuer("desafio.api.test")
                .subject(uuid.toString())
                .expiresAt(Instant.now().plusSeconds(1800))
                .build();
        Jwt fakeJwt = Jwt.withTokenValue("fake-token")
                .header("Test", "none")
                .claims(claims -> claims.putAll(fakeClaims.getClaims()))
                .build();
        fakeTokenValue = fakeJwt.getTokenValue();

        loginRequest = new LoginRequest(
                "test@email.com",
                "password");
        loginResponse = new LoginResponse(
                fakeTokenValue,
                1800L
        );

        user = new User(
                uuid,
                "testemail@email.com",
                "Test User",
                "encodedPassword"
        );
        userCreate = new UserCreateDto(
                "Test User",
                "testemail@email.com",
                "password"
        );
        userResponse = new UserResponseDto(
                "Test User",
                "testemail@email.com"
        );
        userUpdate = new UserUpdateDto(
                "testemail@email.com",
                "Test User"
        );

    }

    @Nested
    class login {
        @Test
        @DisplayName("POST /project-manager/login - should return Login Response when everything is ok")
        void login_returnLoginResponse_WhenEverythingIsOk() {
            doReturn(loginResponse).when(loginService).login(any(LoginRequest.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .post("/project-manager/login")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("expiresAt", equalTo(1800));
            verify(loginService, times(1)).login(loginRequest);
        }
    }

    @Nested
    class create {
        @Test
        @DisplayName("POST /project-manager/users/new - should return User Response when everything is ok")
        void create_returnUserResponse_WhenEverythingIsOk() {
            doReturn(userResponse).when(userService).create(any(UserCreateDto.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(userCreate)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .post("/project-manager/users/new")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("name", equalTo(user.getName()));
            verify(userService, times(1)).create(userCreate);
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("PUT /project-manager/users - should return User Response when everything is ok")
        void update_returnUserResponse_WhenEverythingIsOk() {
            doReturn(userResponse).when(userService).update(any(UserUpdateDto.class), anyString());

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(userUpdate)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .put("/project-manager/users")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("name", equalTo(user.getName()));
            verify(userService, times(1)).update(userUpdate, user.getId().toString());
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("DELETE /project-manager/users - should return No Content when everything is ok")
        void delete_returnNoContent_WhenEverythingIsOk() {
            doNothing().when(userService).delete(anyString());

            RestAssuredMockMvc
                    .given()
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .delete("/project-manager/users")
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
            verify(userService, times(1)).delete(user.getId().toString());
        }
    }
}