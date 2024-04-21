package com.example.springboot.models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContactDto {

   private UUID id;

    private String name;

    private String email;

    private String phone;

    private UserDto user;

}
