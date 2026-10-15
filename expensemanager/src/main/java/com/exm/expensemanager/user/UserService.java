package com.exm.expensemanager.user;

import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO request)
    {
        if(0 >= request.age()) throw new RuntimeException();

        var savedUser = this.userRepository.save(
                this.userMapper.toEntity(request)
        );

        return this.userMapper.toDTO(savedUser);
    }
}
