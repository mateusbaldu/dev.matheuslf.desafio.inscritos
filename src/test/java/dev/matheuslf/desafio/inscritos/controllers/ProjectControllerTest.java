package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.project.ProjectResponseDto;
import dev.matheuslf.desafio.inscritos.services.ProjectService;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(controllers = ProjectController.class)
class ProjectControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProjectService projectService;

    private ProjectCreateDto projectCreateDto;
    private ProjectResponseDto projectResponseDto;
    private User user;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);

        projectCreateDto = new ProjectCreateDto(
                "Test Project",
                "project test description",
                LocalDate.now(),
                LocalDate.MAX
        );

        projectResponseDto = new ProjectResponseDto(
                1L,
                "Test Project",
                "project test description",
                LocalDate.now(),
                LocalDate.MAX
        );

        user = new User(
                UUID.randomUUID(),
                "testemail@email.com",
                "Test User",
                "password"
        );
    }

    @Nested
    class create {
        @Test
        @DisplayName("POST /project-manager/projects - should return Project Response when everything is ok")
        void create_returnProjectResponse_whenEverthingIsOk() {
            doReturn(projectResponseDto).when(projectService).create(any(ProjectCreateDto.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(projectCreateDto)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .post("/project-manager/projects")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", equalTo(1))
                    .body("description", equalTo("project test description"));
            verify(projectService, times(1)).create(projectCreateDto);
        }
    }

    @Nested
    class listAll {
        @Test
        @DisplayName("GET /project-manager/projects - should return Page of Project Response when everything is ok")
        void listAll_returnPageProjectResponse_whenEverthingIsOk() {
            Page<ProjectResponseDto> pageResponse = new PageImpl<>(List.of(projectResponseDto), PageRequest.of(0,10), 1);
            doReturn(pageResponse).when(projectService).listAll(any(Pageable.class));

            RestAssuredMockMvc
                    .given()
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .get("/project-manager/projects")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("totalElements", equalTo(1))
                    .body("content[0].id", equalTo(1));
            verify(projectService, times(1)).listAll(any(Pageable.class));
        }
    }
}