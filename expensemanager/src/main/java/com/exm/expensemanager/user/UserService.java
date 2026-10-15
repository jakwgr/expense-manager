package com.exm.expensemanager.user;

import com.exm.expensemanager.handler.exceptions.WrongAgeException;
import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO request)
    {
        if(0 >= request.age()) throw new WrongAgeException();

        var savedUser = this.userRepository.save(
                this.userMapper.toEntity(request)
        );

        return this.userMapper.toResponseDTO(savedUser);
    }
}
