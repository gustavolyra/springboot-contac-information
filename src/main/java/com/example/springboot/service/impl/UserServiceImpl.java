package com.example.springboot.service.impl;

import com.example.springboot.models.UserModel;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserModel saveUser(UserModel userModel){
        return userRepository.save(userModel);
    }

    @Override
    public List<UserModel> findAllUsers() {
        return StreamSupport.stream(userRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<UserModel> findOne(String id){
        return userRepository.findById(convertIdToUUID(id));
    }

    @Override
    public boolean isExists(UUID id) {
        return userRepository.existsById(id);
    }

    public UUID convertIdToUUID(String id){
        return UUID.fromString(id);
    }

    public void delete(UUID id){
        userRepository.deleteById(id);
    }
}
