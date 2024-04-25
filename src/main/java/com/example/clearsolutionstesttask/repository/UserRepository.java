package com.example.clearsolutionstesttask.repository;

import com.example.clearsolutionstesttask.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsUserByEmail(String email);

    Boolean existsUserByPhone(String phone);

    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to);
}
