package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.dto.ContactsDto;
import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.dto.UserIdDto;
import com.example.clearsolutionstesttask.entity.User;
import com.example.clearsolutionstesttask.exception.BadRequestException;
import com.example.clearsolutionstesttask.exception.NotFoundException;
import com.example.clearsolutionstesttask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Value("${users.minAge}")
    private Integer minAge;

    public UserIdDto createUser(UserDto userDto) {
        String email = userDto.getEmail();
        String phone = userDto.getPhone();
        LocalDate birthDate = userDto.getBirthDate();
        validateBirthDate(birthDate);

        if (existsUserByEmail(email)) {
            throw new BadRequestException("The email is already used");
        }
        if (existsUserByPhone(phone) && phone != null) {
            throw new BadRequestException("The phone is already used");
        }

        User user = User.builder()
                .firstName(userDto.getFirstName())
                .lastName(userDto.getLastName())
                .email(userDto.getEmail())
                .birthDate(userDto.getBirthDate())
                .phone(userDto.getPhone())
                .address(userDto.getAddress())
                .build();
        return new UserIdDto(save(user).getId());
    }

    public void updateContactsInfo(Long userId, ContactsDto contacts) {
        User user = findById(userId);
        user.setPhone(contacts.getPhone());
        user.setAddress(contacts.getAddress());
        save(user);
    }

    public void updateUser(Long userId, UserDto userDto) {
        User user = findById(userId);

        String email = userDto.getEmail();
        String phone = userDto.getPhone();
        LocalDate birthDate = userDto.getBirthDate();
        validateBirthDate(birthDate);

        if (existsUserByEmail(email)) {
            if(!email.equals(user.getEmail()))
                throw new BadRequestException("The email is already used");
        }

        if (existsUserByPhone(phone) && phone != null) {
            if(!phone.equals(user.getPhone()))
                throw new BadRequestException("The phone is already used");
        }
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setBirthDate(userDto.getBirthDate());
        user.setPhone(userDto.getPhone());
        user.setAddress(userDto.getAddress());
        save(user);
    }

    private void validateBirthDate(LocalDate birthDate) {

        if (birthDate.isAfter(LocalDate.now())) {
            throw new BadRequestException("Birthday can not be after now");
        }
        long age = birthDate.until(LocalDate.now(), ChronoUnit.YEARS);
        if (age < minAge) {
            throw new BadRequestException("You must be over 18 years old");
        }

    }

    public void deleteUser(Long userId) {
        User user = findById(userId);
        delete(user);
    }

    public List<UserDto> searchByBirthDateRange(LocalDate from, LocalDate to) {
        return userRepository.findAllByBirthDateBetween(from, to).stream()
                .map(user -> UserDto.builder()
                        .email(user.getEmail())
                        .firstName(user.getFirstName())
                        .lastName(user.getLastName())
                        .birthDate(user.getBirthDate())
                        .phone(user.getPhone())
                        .address(user.getAddress())
                        .build())
                .toList();
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found"));
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    private void delete(User user) {
        userRepository.delete(user);
    }

    private Boolean existsUserByPhone(String phone) {
        return userRepository.existsUserByPhone(phone);
    }

    private Boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    //for testing
    public void setMinAge(Integer minAge) {
        this.minAge = minAge;
    }
}
