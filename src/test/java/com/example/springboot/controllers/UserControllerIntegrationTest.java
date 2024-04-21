package com.example.springboot.controllers;

import com.example.springboot.TestDataUtils;
import com.example.springboot.models.UserModel;
import com.example.springboot.models.dtos.UserDto;
import com.example.springboot.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    private final UserService userService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public UserControllerIntegrationTest(MockMvc mockMvc, UserService userService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.userService = userService;
    }

    @Test
    public void testThatCreateUserSuccessfullyReturnsHttp201() throws Exception {
        UserModel testUser1 = TestDataUtils.createTestUserModel1();

        testUser1.setId(null);
        String userJson = objectMapper.writeValueAsString(testUser1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }


    @Test
    public void testThatCreateUserSuccessfullyReturnsSavedUser() throws Exception {
        UserModel testUser1 = TestDataUtils.createTestUserModel1();

        testUser1.setId(null);
        String userJson = objectMapper.writeValueAsString(testUser1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Maria")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.cpf").value("761.825.500-82")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.birthday").isString()
        );
    }

    @Test
    public void testThatListUserReturnsHttp200() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatListUsersReturnsListOfUsers() throws Exception {
        UserModel testUserEntity = TestDataUtils.createTestUserModel1();
        userService.saveUser(testUserEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].cpf").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].birthday").isString()
        );
    }

    @Test
    public void testThatGetUserReturnsHttp200WhenUserExist() throws Exception {
        UserModel user = TestDataUtils.createTestUserModel1();
        userService.saveUser(user);

        List<UserModel> userList = userService.findAllUsers();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/"+ userList.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

//    @Test
//    public void testThatGetUserReturnsUserWhenExist() throws Exception {
//        UserModel user = TestDataUtils.createTestUserModel1();
//        userService.saveUser(user);
//
//        List<UserModel> userList = userService.findAllUsers();
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.get("/users"+ userList.get(1).getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").value(userList.get(1).getId().toString())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.name").value(userList.get(1).getName())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.cpf").value(userList.get(1).getCpf())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.birthday").isString() //TODO
//        );
//    }

    @Test
    public void testThatGetUserReturnsHttp404WhenUserDoesNotExists() throws Exception {
        final UUID id = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatUpdateUserReturnsHttp404WhenUserDoesNotExists() throws Exception {
        UserDto testUserDto = TestDataUtils.createTestUserDto1();
        String userDtoJson = objectMapper.writeValueAsString(testUserDto);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/"+ UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatFullUpdateUserReturnsHttpStatus4200WhenUserExists() throws Exception {
        UserModel testUserModel = TestDataUtils.createTestUserModel1();
        UserModel savedUser = userService.saveUser(testUserModel);

        UserDto testUserDto = TestDataUtils.createTestUserDto1();
        String userDtoJson = objectMapper.writeValueAsString(testUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatFullUpdateUpdatesExistingUser() throws Exception {
        UserModel testUserModel = TestDataUtils.createTestUserModel1();
        UserModel savedUser = userService.saveUser(testUserModel);

        UserDto testUserDto = TestDataUtils.createTestUserDto1();
        String userDtoJson = objectMapper.writeValueAsString(testUserDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + savedUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userDtoJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedUser.getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedUser.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.cpf").value(savedUser.getCpf())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.birthday").isString() //TODO
        );
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatus204ForNonExistingUser() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    public void testThatDeleteUserReturnsHttpStatus204ForExistingUser() throws Exception {
        UserModel testUserEntityA = TestDataUtils.createTestUserModel1();
        UserModel savedUser = userService.saveUser(testUserEntityA);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + savedUser.getId().toString())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
