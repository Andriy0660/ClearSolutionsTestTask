package com.example.clearsolutionstesttask.controller;

import com.example.clearsolutionstesttask.dto.ContactsDto;
import com.example.clearsolutionstesttask.dto.UserDto;
import com.example.clearsolutionstesttask.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
class UserControllerTest {
    private static UserDto userDto;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private UserService service;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        userDto = UserDto.builder()
                .firstName("Andrii")
                .lastName("Snovyda")
                .email("andrii@gmail.com")
                .birthDate(LocalDate.of(2005, 11, 6))
                .phone("+380971694636")
                .build();
    }

    @Test
    public void shouldSearchByBirthDateRange() throws Exception {

        List<UserDto> users = List.of(userDto);

        when(service.searchByBirthDateRange(Mockito.any(), Mockito.any())).thenReturn(users);

        mvc.perform(get("/api/v1/testTask/users/searchByBirthDateRange")
                        .param("from", LocalDate.of(2004, 11, 6).toString())
                        .param("to", LocalDate.of(2006, 11, 6).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].firstName", is(userDto.getFirstName())));
    }

    @Test
    public void shouldThrowWhenToIsLessThanFrom() throws Exception {

        mvc.perform(get("/api/v1/testTask/users/searchByBirthDateRange")
                        .param("from", LocalDate.of(2006, 11, 6).toString())
                        .param("to", LocalDate.of(2004, 11, 6).toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldThrowWhenDtoIsNotValid() throws Exception {
        userDto.setEmail(null);
        mvc.perform(post("/api/v1/testTask/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldCreateUser() throws Exception {
        mvc.perform(post("/api/v1/testTask/users")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldThrowWhenDtoIsNotValidWhileUpdating() throws Exception {
        userDto.setFirstName(null);
        mvc.perform(put("/api/v1/testTask/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldUpdateUser() throws Exception {
        mvc.perform(put("/api/v1/testTask/users/1")
                        .content(objectMapper.writeValueAsString(userDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldUpdateContacts() throws Exception {
        mvc.perform(patch("/api/v1/testTask/users/1")
                        .content(objectMapper.writeValueAsString
                                (new ContactsDto(null, "Lviv")))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        mvc.perform(delete("/api/v1/testTask/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}