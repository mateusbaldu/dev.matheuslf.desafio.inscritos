package dev.matheuslf.desafio.inscritos.controllers;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.StatusUpdateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.task.TaskResponseDto;
import dev.matheuslf.desafio.inscritos.entities.enums.Priority;
import dev.matheuslf.desafio.inscritos.entities.enums.Status;
import dev.matheuslf.desafio.inscritos.services.TaskService;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.parsing.Parser;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

@WebMvcTest(TaskController.class)
class TaskControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private TaskService taskService;

    private TaskCreateDto taskCreate;
    private TaskResponseDto taskResponse;
    private User user;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mvc);

        user = new User(
                UUID.randomUUID(),
                "testemail@email.com",
                "Test User",
                "password"
        );

        taskResponse = new TaskResponseDto(
                1L,
                "Test task",
                "Test task description",
                Priority.HIGH,
                Status.TODO,
                LocalDate.MAX,
                1L
        );

        taskCreate = new TaskCreateDto(
                "Test task",
                "Test task description",
                Priority.HIGH,
                Status.TODO,
                LocalDate.MAX,
                1L
        );
    }

    @Nested
    class create {
        @Test
        @DisplayName("POST /project-manager/tasks - should return Task Response when everything is ok")
        void create_returnTaskResponse_whenEverythingIsOk() {
            doReturn(taskResponse).when(taskService).create(any(TaskCreateDto.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(taskCreate)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .post("/project-manager/tasks")
                    .then()
                    .statusCode(HttpStatus.CREATED.value())
                    .body("id", equalTo(1))
                    .body("description", equalTo("Test task description"));
            verify(taskService, times(1)).create(taskCreate);
        }
    }

    @Nested
    class findTasks {
        @Test
        @DisplayName("GET /project-manager/tasks - should return Page of Task Response when everything is ok")
        void findTasks_returnPageTaskResponse_whenEverythingIsOk() {
            List<TaskResponseDto> list = new ArrayList<>();
            list.add(taskResponse);
            Page<TaskResponseDto> pageResponse = new PageImpl<>(list, PageRequest.of(0, 10), 1);
            doReturn(pageResponse).when(taskService).findTasks(any(), any(), anyLong(), any());

            RestAssuredMockMvc
                    .given()
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .get("/project-manager/tasks")
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("totalElements", equalTo(1))
                    .body("content[0].id", equalTo(1))
                    .body("content[0].description", equalTo("Test task description"));
            verify(taskService, times(1)).findTasks(eq(Status.TODO), eq(Priority.HIGH), anyLong(), any());
        }
    }

    @Nested
    class updateStatus {
        @Test
        @DisplayName("PUT /project-manager/tasks/{id}/status - should return Task Response when everything is ok")
        void updateStatus_returnTaskResponse_whenEverythingIsOk() {
            StatusUpdateDto dto = new StatusUpdateDto(
                    Status.TODO
            );
            doReturn(taskResponse).when(taskService).updateStatus(anyLong(), any(Status.class));

            RestAssuredMockMvc
                    .given()
                    .contentType(ContentType.JSON)
                    .body(dto)
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .put("/project-manager/tasks/{id}/status", 1L)
                    .then()
                    .statusCode(HttpStatus.OK.value())
                    .body("id", equalTo(1))
                    .body("description", equalTo("Test task description"))
                    .body("status", equalTo(Status.TODO.name()));
            verify(taskService, times(1)).updateStatus(1L, dto.status());
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("DELETE /project-manager/tasks/{id} - should return No Content when everything is ok")
        void delete_returnNoContent_whenEverythingIsOk() {
            doNothing().when(taskService).delete(anyLong());

            RestAssuredMockMvc
                    .given()
                    .postProcessors(
                            jwt().jwt(j -> j.subject(user.getId().toString())),
                            csrf()
                    )
                    .when()
                    .delete("/project-manager/tasks/{id}", 1L)
                    .then()
                    .statusCode(HttpStatus.NO_CONTENT.value());
            verify(taskService, times(1)).delete(1L);
        }
    }
}