package com.exm.expensemanager.user;

import com.exm.expensemanager.common.CurrencyInfoDTO;
import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.handler.exceptions.user.UserNotCreatedException;
import com.exm.expensemanager.handler.exceptions.user.UserNotExistsException;
import com.exm.expensemanager.handler.exceptions.WrongAgeException;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.handler.exceptions.user.UserNotUpdatedException;
import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import com.exm.expensemanager.user.dto.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Util util;

    public UserResponseDTO createUser(UserRequestDTO request)
    {
        if (0 >= request.age()) throw new WrongAgeException();

        User savedUser;

        if(!util.doesCurrencyContains(String.valueOf(request.currency())))
        {
            throw new WrongCurrencyException();
        }

        UserRequestDTO requestUP = new UserRequestDTO(
                request.name(), request.lastName(), request.age(), request.currency().toUpperCase());

        try {
            savedUser = this.userRepository.save(this.userMapper.toEntity(requestUP));
        } catch (Exception e) {
            throw new UserNotCreatedException();
        }
        return this.userMapper.toResponseDTO(savedUser);
    }

    public UserResponseDTO updateUser(UserUpdateRequestDTO request)
    {
        var id = this.util.getCurrentUserId();
        User user = this.userRepository.findById(id).orElseThrow(UserNotExistsException::new);
        User updatedUser;

        if(request.name() != null) user.setName(request.name());
        if(request.lastName() != null) user.setLastName(request.lastName());
        if(request.age() != null)
            if(request.age() <= 0)
            {
                throw new WrongAgeException();
            }
        else { user.setAge(request.age()); }
        if(request.currency() != null)
        {
            if(util.doesCurrencyContains(String.valueOf(request.currency())))
            {
                user.setCurrency(Currency.valueOf(request.currency().toUpperCase()));
            }
            else
            {
                throw new WrongCurrencyException();
            }
        }

        try{
            updatedUser =  this.userRepository.save(user);
        } catch (RuntimeException e) {
            throw new UserNotUpdatedException();
        }

        return this.userMapper.toResponseDTO(updatedUser);
    }

    public CurrencyInfoDTO getUserCurrencyInformation(){
        return this.util.getUserCurrencyInfo();
    }
}
