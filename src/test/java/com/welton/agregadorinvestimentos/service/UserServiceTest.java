package com.welton.agregadorinvestimentos.service;

import com.welton.agregadorinvestimentos.controller.CreateUserDto;
import com.welton.agregadorinvestimentos.controller.UpdateUserDto;
import com.welton.agregadorinvestimentos.entity.User;
import com.welton.agregadorinvestimentos.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // Preparar
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Captor
    private ArgumentCaptor<UUID> uuidArgumentCaptor;

    @Nested
    class createUser {

        @Test
        @DisplayName("Should create a user with success")
        void shouldCreateAUserWithSuccess() {

            var user = new User(
                    UUID.randomUUID(),
                    "userName",
                    "email@gmail.com",
                    "12345",
                    Instant.now(),
                    null
            );
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());
            var input = new CreateUserDto("Welton", "welton@gmail.com", "12345678");

            var output = userService.createUser(input);

            assertNotNull(output);

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(input.userName(), userCaptured.getUserName());
            assertEquals(input.email(), userCaptured.getEmail());
            assertEquals(input.password(), userCaptured.getPassword());
        }

        @Test
        @DisplayName("Should throw Exception when error occurs")
        void shouldThrowExceptionWhenErrorOccurs() {
            doThrow(new RuntimeException()).when(userRepository).save(any());
            var input = new CreateUserDto("Welton", "welton@gmail.com", "12345678");

            assertThrows(RuntimeException.class, () -> userService.createUser(input));
        }
    }
    
    @Nested
    class getUserById {

        @Test
        @DisplayName("Should get user by id with success when optional is present")
        void shouldGetUserByIdWithSuccessWhenOptionalIsPresent() {
            var user = new User(
              UUID.randomUUID(),
              "Welton",
              "wsamp@gmail.com",
              "1234",
              Instant.now(),
              null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(user.getUserId().toString());

            assertTrue(output.isPresent());
            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());
        }

        @Test
        @DisplayName("Should get user by id with success when optional is empty")
        void shouldGetUserByIdWithSuccessWhenOptionalIsEmpty() {
            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            var output = userService.getUserById(userId.toString());

            assertTrue(output.isEmpty());
            assertEquals(userId, uuidArgumentCaptor.getValue());
        }
    }
    
    @Nested
    class listUser {
        @Test
        @DisplayName("Should return all users all with success")
        void shouldReturnAllUsersWithSuccess() {

            var user = new User(
                    UUID.randomUUID(),
                    "Welton",
                    "wsamp@gmail.com",
                    "1234",
                    Instant.now(),
                    null
            );
            var userList = List.of(user);
            doReturn(userList).when(userRepository).findAll();

            var output = userService.listUsers();

            assertNotNull(output);
            assertEquals(userList.size(), output.size());
        }
    }
    
    @Nested
    class deleteById {
        @Test
        @DisplayName("Should delete user with success when user exists")
        void shouldDeleteUserWithSuccessWhenUserExistis() {

            doReturn(true).when(userRepository).existsById(uuidArgumentCaptor.capture());
            doNothing().when(userRepository).deleteById(uuidArgumentCaptor.capture());

            var userId = UUID.randomUUID();
            userService.deleteById(userId.toString());

            var idList = uuidArgumentCaptor.getAllValues();
            assertEquals(userId, idList.get(0));
            assertEquals(userId, idList.get(1));

            verify(userRepository, times(1)).existsById(idList.get(0));
            verify(userRepository, times(1)).deleteById(idList.get(1));
        }

        @Test
        @DisplayName("Should not delete user when user not exists")
        void shouldNotDeleteUserWhenUserNotExistis() {

            doReturn(false).when(userRepository).existsById(uuidArgumentCaptor.capture());
            var userId = UUID.randomUUID();

            userService.deleteById(userId.toString());

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).existsById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).deleteById(any());
        }
    }

    @Nested
    class updateUserById {
        @Test
        @DisplayName("Should update user by id when user exists and userName and password is filled")
        void shouldUpdateUserByIdWhenUserExistisAndUserNameAndPasswordIsFilled() {

            var updateUserDto = new UpdateUserDto(
                    "Nicole",
                    "nicole@gmail.com",
                    "12345"
            );

            var user = new User(
                    UUID.randomUUID(),
                    "Welton",
                    "wsamp@gmail.com",
                    "1234",
                    Instant.now(),
                    null
            );
            doReturn(Optional.of(user)).when(userRepository).findById(uuidArgumentCaptor.capture());
            doReturn(user).when(userRepository).save(userArgumentCaptor.capture());

            userService.updateUserById(user.getUserId().toString(), updateUserDto);

            assertEquals(user.getUserId(), uuidArgumentCaptor.getValue());

            var userCaptured = userArgumentCaptor.getValue();

            assertEquals(updateUserDto.userName(), userCaptured.getUserName());
            assertEquals(updateUserDto.email(), userCaptured.getEmail());
            assertEquals(updateUserDto.password(), userCaptured.getPassword());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(1)).save(user);
        }

        @Test
        @DisplayName("Should not update user when user not exists")
        void shouldNotUpdateUserWhenUserNotExistis() {

            var updateUserDto = new UpdateUserDto(
                    "Nicole",
                    "nicole@gmail.com",
                    "12345"
            );

            var userId = UUID.randomUUID();
            doReturn(Optional.empty()).when(userRepository).findById(uuidArgumentCaptor.capture());

            userService.updateUserById(userId.toString(), updateUserDto);

            assertEquals(userId, uuidArgumentCaptor.getValue());

            verify(userRepository, times(1)).findById(uuidArgumentCaptor.getValue());
            verify(userRepository, times(0)).save(any());
        }
    }

}