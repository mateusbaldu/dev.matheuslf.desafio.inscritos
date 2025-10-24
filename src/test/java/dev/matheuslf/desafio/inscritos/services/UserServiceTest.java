package dev.matheuslf.desafio.inscritos.services;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserCreateDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserResponseDto;
import dev.matheuslf.desafio.inscritos.entities.dtos.user.UserUpdateDto;
import dev.matheuslf.desafio.inscritos.exceptions.ResourceNotFoundException;
import dev.matheuslf.desafio.inscritos.mappers.UserMapper;
import dev.matheuslf.desafio.inscritos.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;
    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserCreateDto userCreate;
    private UserResponseDto userResponse;
    private UserUpdateDto userUpdate;

    @BeforeEach
    void setUp() {
        UUID uuid = UUID.randomUUID();
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
    class create {
        @Test
        @DisplayName("should return a User Response when everything is ok")
        void create_returnUserResponse_whenEverythingIsOk() {
            String encodedPassword = "encodedPassword";
            String userPassword = userCreate.password();

            doNothing().when(userMapper).createMapping(eq(userCreate) , any(User.class));
            doReturn(encodedPassword).when(passwordEncoder).encode(userPassword);
            doReturn(user).when(userRepository).save(any(User.class));
            doReturn(userResponse).when(userMapper).responseMapping(any(User.class));

            var output = userService.create(userCreate);

            assertNotNull(output);
            assertEquals(output, userResponse);
            verify(userRepository, times(1)).save(any(User.class));
            verify(userMapper, times(1)).createMapping(eq(userCreate), any(User.class));
            verify(passwordEncoder, times(1)).encode(userCreate.password());
            verify(userMapper, times(1)).responseMapping(user);
        }
    }

    @Nested
    class update {
        @Test
        @DisplayName("Should return User Response when everything is ok")
        void update_returnUserResponse_whenEverythingIsOk() {
            doReturn(Optional.of(user)).when(userRepository).findById(any(UUID.class));
            doNothing().when(userMapper).updateMapping(any(UserUpdateDto.class), any(User.class));
            doReturn(user).when(userRepository).save(any(User.class));
            doReturn(userResponse).when(userMapper).responseMapping(any(User.class));

            var output = userService.update(userUpdate, user.getId().toString());

            assertNotNull(output);
            assertEquals(output, userResponse);
            verify(userRepository, times(1)).findById(user.getId());
            verify(userMapper, times(1)).updateMapping(eq(userUpdate), any(User.class));
            verify(userMapper, times(1)).responseMapping(user);
        }

        @Test
        @DisplayName("Should throw a Exception when the user isn't found")
        void update_throwResourceNotFoundException_whenUserIsNotFound() {
            doReturn(Optional.empty()).when(userRepository).findById(any(UUID.class));

            assertThrows(ResourceNotFoundException.class, () -> userService.update(userUpdate, user.getId().toString()));
        }
    }

    @Nested
    class delete {
        @Test
        @DisplayName("Should return void when everything is ok")
        void delete_returnVoid_whenEverythingIsOk() {
            doReturn(Optional.of(user)).when(userRepository).findById(any(UUID.class));
            doNothing().when(userRepository).delete(any(User.class));

            userService.delete(user.getId().toString());

            verify(userRepository, times(1)).findById(user.getId());
            verify(userRepository, times(1)).delete(user);
        }

        @Test
        @DisplayName("Should throw a Exception when the user isn't found")
        void delete_throwResourceNotFoundException_whenUserIsNotFound() {
            doReturn(Optional.empty()).when(userRepository).findById(any(UUID.class));

            assertThrows(ResourceNotFoundException.class, () -> userService.delete(user.getId().toString()));
        }
    }
}