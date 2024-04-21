package com.example.springboot.mappers.impl;

import com.example.springboot.mappers.Mapper;
import com.example.springboot.models.UserModel;
import com.example.springboot.models.dtos.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements Mapper<UserModel, UserDto> {

    private final ModelMapper modelMapper;

    public UserMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }

    @Override
    public UserDto mapTo(UserModel userModel) {
        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public UserModel mapFrom(UserDto userDto) {
        return modelMapper.map(userDto, UserModel.class);
    }
}
