package com.example.springboot.service;

import com.example.springboot.models.UserModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {

    UserModel saveUser(UserModel userModel);

    List<UserModel> findAllUsers();

    Optional<UserModel> findOne(String id);

    boolean isExists(UUID id);

    UUID convertIdToUUID(String id);

    void delete(UUID id);
}