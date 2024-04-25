package com.example.clearsolutionstesttask.service;

import com.example.clearsolutionstesttask.dto.ContactsDto;
import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.dto.UserIdDto;
import com.example.clearsolutionstesttask.entity.User;
import com.example.clearsolutionstesttask.exception.BadRequestException;
import com.example.clearsolutionstesttask.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private static UserDto userDto;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private Integer minAge = 18;

    @BeforeEach
    void setUp() {
        userService.setMinAge(minAge);
        userDto = UserDto.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("andrii@gmail.com")
                .birthDate(LocalDate.of(2005, 11, 6))
                .phone("+380971694636")
                .build();
    }

    @Test
    void shouldThrowWhenEmailExists() {
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(true);
        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The email is already used");
    }

    @Test
    void shouldThrowWhenPhoneExists() {
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByPhone(userDto.getPhone())).thenReturn(true);
        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The phone is already used");
    }

    @Test
    void shouldThrowWhenBirthDateAfterNow() {
        userDto.setBirthDate(LocalDate.of(2025, 11, 6));
        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Birthday can not be after now");
    }

    @Test
    void shouldThrowWhenAgeLessThan18() {
        userDto.setBirthDate(LocalDate.of(2010, 11, 6));
        assertThatThrownBy(() -> userService.createUser(userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("You must be over 18 years old");
    }

    @Test
    void shouldCreateUser() {
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByPhone(userDto.getPhone())).thenReturn(false);
        when(userRepository.save(any())).thenReturn(User.builder().id(1L).build());
        UserIdDto userIdDto = userService.createUser(userDto);
        verify(userRepository).save(any());
        assertThat(userIdDto.getId()).isEqualTo(1L);
    }

    @Test
    void shouldUpdateContactsInfo() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        userService.updateContactsInfo(any(), new ContactsDto("+381111111111", null));
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User user = captor.getValue();
        assertThat(user.getPhone()).isEqualTo("+381111111111");

    }

    @Test
    void shouldDeleteUser() {
        User user = User.builder().id(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.deleteUser(user.getId());
        verify(userRepository).delete(user);
    }

    @Test
    void shouldSearchByBirthDateRange() {
        LocalDate from = LocalDate.of(2004, 11, 6);
        LocalDate to = LocalDate.of(2006, 11, 6);
        when(userRepository.findAllByBirthDateBetween(from, to))
                .thenReturn(List.of(User.builder().email(userDto.getEmail()).build()));

        List<UserDto> users = userService.searchByBirthDateRange(from, to);
        verify(userRepository).findAllByBirthDateBetween(from, to);
        assertThat(users.size()).isEqualTo(1);
        assertThat(users.get(0).getEmail()).isEqualTo(userDto.getEmail());
    }

    @Test
    void shouldThrowWhenEmailExistsWhileUpdate() {
        User user = User.builder().id(1L).phone("+380971674530").email("inna@gmail.com").build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(user.getId(), userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The email is already used");

    }
    @Test
    void shouldThrowWhenPhoneExistsWhileUpdate() {
        User user = User.builder().id(1L).phone("+380971674530").email("inna@gmail.com").build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByPhone(userDto.getPhone())).thenReturn(true);

        assertThatThrownBy(() -> userService.updateUser(user.getId(), userDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("The phone is already used");

    }
    @Test
    void shouldUpdateUser() {
        when(userRepository.findById(any())).thenReturn(Optional.of(new User()));
        when(userRepository.existsUserByEmail(userDto.getEmail())).thenReturn(false);
        when(userRepository.existsUserByPhone(userDto.getPhone())).thenReturn(false);
        userService.updateUser(any(), userDto);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User captorUser = captor.getValue();
        assertThat(captorUser.getEmail()).isEqualTo(userDto.getEmail());

    }


}