package com.exm.expensemanager.user;

import com.exm.expensemanager.user.dto.UserRequestDTO;
import com.exm.expensemanager.user.dto.UserResponseDTO;
import com.exm.expensemanager.user.dto.UserUpdateRequestDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO request)
    {
        UserResponseDTO response = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping()
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserUpdateRequestDTO request)
    {
        UserResponseDTO response = userService.updateUser(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }
}
