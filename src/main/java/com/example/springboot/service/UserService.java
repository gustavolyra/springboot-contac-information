package com.example.springboot.service;

import com.example.springboot.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserModel saveUser(UserModel userModel);

    List<UserModel> findAllUsers();

    Page<UserModel> findAll(Pageable pageable);

    Boolean isBirthDayValid(Date birthday);

    Optional<UserModel> findOne(String id);

    Boolean isExists(UUID id);

    UUID convertIdToUUID(String id);

    void delete(UUID id);
}