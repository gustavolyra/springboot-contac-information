package com.example.springboot.controllers;

import com.example.springboot.mappers.Mapper;
import com.example.springboot.models.UserModel;
import com.example.springboot.models.dtos.UserDto;
import com.example.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class UserController {

    private final UserService userService;

    private final Mapper<UserModel, UserDto> userMapper;

    public UserController(UserService userService, Mapper<UserModel, UserDto> userMapper){
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/users/register")
    public ResponseEntity registerUser(@RequestBody @Valid UserDto userDto){
        UserModel userModel = userMapper.mapFrom(userDto);
        if(userService.isBirthDayValid(userModel.getBirthday())) {
            UserModel savedUserModel = userService.saveUser(userModel);
            return new ResponseEntity<>(userMapper.mapTo(savedUserModel), HttpStatus.CREATED);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: Date should not be in the future.");
    }

    @GetMapping("/users")
    public List<UserDto> getAllUser(){
        List<UserModel> usersList = userService.findAllUsers();
        return usersList.stream().map(userMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getAnUser(@PathVariable("id") String id){
        Optional<UserModel> user = userService.findOne(id);
        return user.map(userModel -> {
            UserDto userDto = userMapper.mapTo(userModel);
            return new ResponseEntity<>(userDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") String id, @RequestBody @Valid UserDto userDto){
        final UUID id_uudi = userService.convertIdToUUID(id);

        if(!userService.isExists(id_uudi)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        userDto.setId(id_uudi);
        UserModel userModel = userMapper.mapFrom(userDto);
        UserModel savedUser = userService.saveUser(userModel);

        return new ResponseEntity<>(userMapper.mapTo(savedUser), HttpStatus.OK);
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity deleteContact(@PathVariable("id") String id){
        userService.delete(userService.convertIdToUUID(id));
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }


}
