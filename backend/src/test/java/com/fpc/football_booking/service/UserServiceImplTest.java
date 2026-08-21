package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.UserMapper;
import com.fpc.football_booking.repository.UserRepository;
import com.fpc.football_booking.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    private UserRequestDto requestDto;

    private User user;

    private UserResponseDto responseDto;


    @BeforeEach
    void setUp() {

        requestDto = new UserRequestDto();

        requestDto.setEmail("mario@email.it");
        requestDto.setTelefono("3331234567");
        requestDto.setPassword("password123");


        user = new User();

        user.setId(1L);
        user.setEmail("mario@email.it");
        user.setTelefono("3331234567");


        responseDto = new UserResponseDto();
    }


    // =========================================================
    // CREATE USER
    // =========================================================

    @Test
    void shouldCreateUserSuccessfully() {

        when(userRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(false);

        when(userRepository.existsByTelefono(
                requestDto.getTelefono()
        )).thenReturn(false);


        when(userMapper.toEntity(
                requestDto
        )).thenReturn(user);


        when(passwordEncoder.encode(
                requestDto.getPassword()
        )).thenReturn("encodedPassword");


        when(userRepository.save(
                user
        )).thenReturn(user);


        when(userMapper.toResponseDto(
                user
        )).thenReturn(responseDto);


        UserResponseDto result =
                userService.createUser(requestDto);


        assertNotNull(result);

        assertSame(
                responseDto,
                result
        );


        assertEquals(
                "encodedPassword",
                user.getPassword()
        );


        assertEquals(
                Role.CLIENTE,
                user.getRuolo()
        );


        verify(userRepository)
                .existsByEmail(
                        requestDto.getEmail()
                );


        verify(userRepository)
                .existsByTelefono(
                        requestDto.getTelefono()
                );


        verify(passwordEncoder)
                .encode(
                        requestDto.getPassword()
                );


        verify(userRepository)
                .save(user);


        verify(userMapper)
                .toResponseDto(user);

    }


    // =========================================================
    // EMAIL GIÀ REGISTRATA
    // =========================================================

    @Test
    void shouldThrowConflictWhenEmailAlreadyExists() {

        when(userRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(true);


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () ->
                                userService.createUser(
                                        requestDto
                                )
                );


        assertEquals(
                "Email already registered",
                exception.getMessage()
        );


        verify(userRepository)
                .existsByEmail(
                        requestDto.getEmail()
                );


        verify(userRepository, never())
                .existsByTelefono(any());


        verify(userRepository, never())
                .save(any());


        verify(passwordEncoder, never())
                .encode(any());

    }


    // =========================================================
    // TELEFONO GIÀ REGISTRATO
    // =========================================================

    @Test
    void shouldThrowConflictWhenPhoneAlreadyExists() {

        when(userRepository.existsByEmail(
                requestDto.getEmail()
        )).thenReturn(false);


        when(userRepository.existsByTelefono(
                requestDto.getTelefono()
        )).thenReturn(true);


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () ->
                                userService.createUser(
                                        requestDto
                                )
                );


        assertEquals(
                "Phone number already registered",
                exception.getMessage()
        );


        verify(userRepository)
                .existsByEmail(
                        requestDto.getEmail()
                );


        verify(userRepository)
                .existsByTelefono(
                        requestDto.getTelefono()
                );


        verify(userRepository, never())
                .save(any());


        verify(passwordEncoder, never())
                .encode(any());


        verify(userMapper, never())
                .toEntity(any());

    }


    // =========================================================
    // PASSWORD CODIFICATA
    // =========================================================

    @Test
    void shouldEncodePasswordBeforeSavingUser() {

        when(userRepository.existsByEmail(any()))
                .thenReturn(false);

        when(userRepository.existsByTelefono(any()))
                .thenReturn(false);


        when(userMapper.toEntity(
                requestDto
        )).thenReturn(user);


        when(passwordEncoder.encode(
                "password123"
        )).thenReturn("encodedPassword");


        when(userRepository.save(
                user
        )).thenReturn(user);


        when(userMapper.toResponseDto(
                user
        )).thenReturn(responseDto);


        userService.createUser(requestDto);


        verify(passwordEncoder)
                .encode("password123");


        assertEquals(
                "encodedPassword",
                user.getPassword()
        );

    }


    // =========================================================
    // RUOLO CLIENTE
    // =========================================================

    @Test
    void shouldSetClienteRoleWhenCreatingUser() {

        when(userRepository.existsByEmail(any()))
                .thenReturn(false);

        when(userRepository.existsByTelefono(any()))
                .thenReturn(false);


        when(userMapper.toEntity(
                requestDto
        )).thenReturn(user);


        when(passwordEncoder.encode(any()))
                .thenReturn("encodedPassword");


        when(userRepository.save(
                user
        )).thenReturn(user);


        when(userMapper.toResponseDto(
                user
        )).thenReturn(responseDto);


        userService.createUser(requestDto);


        assertEquals(
                Role.CLIENTE,
                user.getRuolo()
        );

    }


    // =========================================================
    // GET ALL USERS
    // =========================================================

    @Test
    void shouldReturnAllUsers() {

        User user2 = new User();

        user2.setId(2L);
        user2.setEmail("luigi@email.it");


        UserResponseDto responseDto2 =
                new UserResponseDto();


        when(userRepository.findAll())
                .thenReturn(
                        List.of(
                                user,
                                user2
                        )
                );


        when(userMapper.toResponseDto(user))
                .thenReturn(responseDto);


        when(userMapper.toResponseDto(user2))
                .thenReturn(responseDto2);


        List<UserResponseDto> result =
                userService.getAllUsers();


        assertNotNull(result);

        assertEquals(
                2,
                result.size()
        );


        assertEquals(
                responseDto,
                result.get(0)
        );


        assertEquals(
                responseDto2,
                result.get(1)
        );


        verify(userRepository)
                .findAll();


        verify(userMapper)
                .toResponseDto(user);


        verify(userMapper)
                .toResponseDto(user2);

    }


    // =========================================================
    // GET USER BY ID
    // =========================================================

    @Test
    void shouldReturnUserById() {

        when(userRepository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(userMapper.toResponseDto(
                user
        )).thenReturn(responseDto);


        UserResponseDto result =
                userService.getUserById(1L);


        assertNotNull(result);

        assertSame(
                responseDto,
                result
        );


        verify(userRepository)
                .findById(1L);


        verify(userMapper)
                .toResponseDto(user);

    }


    // =========================================================
    // GET USER BY ID - NON TROVATO
    // =========================================================

    @Test
    void shouldThrowExceptionWhenUserNotFoundById() {

        when(userRepository.findById(99L))
                .thenReturn(
                        Optional.empty()
                );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                userService.getUserById(99L)
                );


        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(userRepository)
                .findById(99L);


        verify(userMapper, never())
                .toResponseDto(any());

    }


    // =========================================================
    // DELETE USER
    // =========================================================

    @Test
    void shouldDeleteUser() {

        when(userRepository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        userService.deleteUser(1L);


        verify(userRepository)
                .findById(1L);


        verify(userRepository)
                .delete(user);

    }


    // =========================================================
    // DELETE USER - NON TROVATO
    // =========================================================

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingUser() {

        when(userRepository.findById(99L))
                .thenReturn(
                        Optional.empty()
                );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                userService.deleteUser(99L)
                );


        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(userRepository)
                .findById(99L);


        verify(userRepository, never())
                .delete(any());

    }

}
