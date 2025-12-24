package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.configs.RateLimitFilter;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginRequest;
import dev.matheuslf.desafio.inscritos.entities.dtos.login.LoginResponse;
import dev.matheuslf.desafio.inscritos.services.LoginService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(RateLimitFilter.class)
class LoginControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private RateLimitFilter rateLimitFilter;

    @MockitoBean
    private LoginService loginService;

    private LoginRequest loginRequest;
    private LoginResponse loginResponse;

    @BeforeEach
    void setUp() {
        rateLimitFilter.resetCache();

        RestAssuredMockMvc.mockMvc(mvc);

        loginRequest = new LoginRequest(
                "test@email.com",
                "password");
        loginResponse = new LoginResponse(
                "fake-token",
                1800L
        );
    }

    @Nested
    class login {
        @Test
        @DisplayName("POST /login - should return Login Response when everything is ok")
        void login_returnLoginResponse_WhenEverythingIsOk() {
            doReturn(loginResponse).when(loginService).login(any(LoginRequest.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(loginRequest)
                    .when()
                    .post("/login")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("expiresAt", equalTo(1800));
            verify(loginService, times(1)).login(loginRequest);
        }
    }
}
