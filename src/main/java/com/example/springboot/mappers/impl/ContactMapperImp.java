package com.example.springboot.mappers.impl;

import com.example.springboot.mappers.Mapper;
import com.example.springboot.models.ContactModel;
import com.example.springboot.models.dtos.ContactDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ContactMapperImp implements Mapper<ContactModel, ContactDto> {

    private final ModelMapper modelMapper;

    public ContactMapperImp(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public ContactDto mapTo(ContactModel contactModel) {
        return modelMapper.map(contactModel, ContactDto.class);
    }

    @Override
    public ContactModel mapFrom(ContactDto contactDto) {
        return modelMapper.map(contactDto, ContactModel.class);
    }
}