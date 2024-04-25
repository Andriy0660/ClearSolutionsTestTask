package com.example.clearsolutionstesttask.controller;

import com.example.clearsolutionstesttask.dto.ContactsDto;
import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.dto.UserIdDto;
import com.example.clearsolutionstesttask.exception.BadRequestException;
import com.example.clearsolutionstesttask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/testTask/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserIdDto> createUser(@Valid @RequestBody UserDto userDto) {
        UserIdDto userId = userService.createUser(userDto);
        return ResponseEntity.ok(userId);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(@PathVariable Long userId,
                                           @Valid @RequestBody UserDto userDto) {
        userService.updateUser(userId, userDto);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Void> updateContactsInfo(@PathVariable Long userId,
                                                   @RequestBody ContactsDto contacts) {
        userService.updateContactsInfo(userId, contacts);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/searchByBirthDateRange")
    public ResponseEntity<List<UserDto>> searchByBirthDateRange(
            @RequestParam(name = "from") LocalDate from,
            @RequestParam(name = "to") LocalDate to) {

        if (from.isAfter(to))
            throw new BadRequestException("`From` must be less than `To`");

        List<UserDto> users = userService.searchByBirthDateRange(from, to);
        return ResponseEntity.ok(users);
    }

}
