package com.example.springboot.service.impl;

import com.example.springboot.models.UserModel;
import com.example.springboot.repositories.UserRepository;
import com.example.springboot.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
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

    public Boolean isBirthDayValid(Date birthday){
        LocalDate localDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        LocalDate today = LocalDate.now();

        return localDate.isBefore(today);
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
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Optional<UserModel> findOne(String id){
        return userRepository.findById(convertIdToUUID(id));
    }

    @Override
    public Boolean isExists(UUID id) {
        return userRepository.existsById(id);
    }

    public UUID convertIdToUUID(String id){
        return UUID.fromString(id);
    }

    public void delete(UUID id){
        userRepository.deleteById(id);
    }
}
