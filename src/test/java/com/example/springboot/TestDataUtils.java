package com.example.springboot;

import com.example.springboot.models.ContactModel;
import com.example.springboot.models.UserModel;
import com.example.springboot.models.dtos.ContactDto;
import com.example.springboot.models.dtos.UserDto;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

public final class TestDataUtils {

    private TestDataUtils(){
    }

    public static UserModel createTestUserModel1() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserModel.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .cpf("761.825.500-82")
                .birthday(dataFormat.parse("1999-01-01"))
                .build();
    }

    public static UserDto createTestUserDto1() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserDto.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .cpf("761.825.500-82")
                .birthday(dataFormat.parse("1999-01-01"))
                .build();
    }

    public static UserModel createTestUserModelInvalidCPF() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserModel.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .cpf("761.825.500-8")
                .birthday(dataFormat.parse("1999-01-01"))
                .build();
    }

    public static UserModel createTestUserModelNullCPF() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserModel.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .cpf("761.825.500-8")
                .birthday(dataFormat.parse("1999-01-01"))
                .build();
    }

    public static UserModel createTestUserModelNullName() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserModel.builder()
                .id(UUID.randomUUID())
                .name(null)
                .cpf("761.825.500-82")
                .birthday(dataFormat.parse("1999-01-01"))
                .build();
    }

    public static UserModel createTestUserModelFutureBirthday() throws ParseException {
        DateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        return UserModel.builder()
                .id(UUID.randomUUID())
                .name("Maria")
                .cpf("761.825.500-82")
                .birthday(dataFormat.parse("3999-01-01"))
                .build();
    }

    public static ContactModel createTestContactModel1(final UserModel userModel){
        return ContactModel.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .phone("99 99999-9999")
                .name("Contact A")
                .user(userModel)
                .build();
    }

    public static ContactDto createTestContactDtoInvalidEmail(final UserDto userDto){
        return ContactDto.builder()
                .email("test")
                .phone("99 99999-9999")
                .name("Contact A")
                .user(userDto)
                .build();
    }

    public static ContactDto createTestContactDtoEmailNull(final UserDto userDto){
        return ContactDto.builder()
                .email(null)
                .phone("99 99999-9999")
                .name("Contact A")
                .user(userDto)
                .build();
    }

    public static ContactDto createTestContactDtoPhoneNull(final UserDto userDto){
        return ContactDto.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .phone(null)
                .name("Contact A")
                .user(userDto)
                .build();
    }

    public static ContactDto createTestContactDtoNameNull(final UserDto userDto){
        return ContactDto.builder()
                .id(UUID.randomUUID())
                .email("test@test.com")
                .phone("99 99999-9999")
                .name(null)
                .user(userDto)
                .build();
    }

    public static ContactDto createTestContactDto(final UserDto userDto){
        return ContactDto.builder()
                .email("test@test.com")
                .phone("99 99999-9999")
                .name("Contact A")
                .user(userDto)
                .build();
    }
}
