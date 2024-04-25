package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.dto.ContactsDto;
import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.dto.UserIdDto;

import java.time.LocalDate;
import java.util.List;

public interface UserService {
    UserIdDto createUser(UserDto userDto);
    void updateContactsInfo(Long userId, ContactsDto contacts);
    void updateUser(Long userId, UserDto userDto);
    void deleteUser(Long userId);
    List<UserDto> searchByBirthDateRange(LocalDate from, LocalDate to);
}
