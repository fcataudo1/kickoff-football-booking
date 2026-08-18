package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.UserMapper;
import com.fpc.football_booking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    @Transactional
    public UserResponseDto createUser(
            UserRequestDto dto
    ) {

        if (userRepository.existsByEmail(dto.getEmail())) {

            throw new ConflictException(
                    "Email already registered"
            );
        }


        if (userRepository.existsByTelefono(dto.getTelefono())) {

            throw new ConflictException(
                    "Phone number already registered"
            );
        }


        User user = userMapper.toEntity(dto);

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        user.setRuolo(
                Role.CLIENTE
        );

        User saved =
                userRepository.save(user);


        return userMapper.toResponseDto(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponseDto)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public UserResponseDto getUserById(
            Long id
    ) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        return userMapper.toResponseDto(user);
    }


    @Override
    @Transactional
    public void deleteUser(
            Long id
    ) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                )
                        );


        userRepository.delete(user);
    }
}
