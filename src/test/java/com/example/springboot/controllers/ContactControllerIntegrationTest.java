package com.example.springboot.controllers;

import com.example.springboot.TestDataUtils;
import com.example.springboot.models.ContactModel;
import com.example.springboot.models.dtos.ContactDto;
import com.example.springboot.service.ContactService;
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
public class ContactControllerIntegrationTest {

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    private final ContactService contactService;

    @Autowired
    public ContactControllerIntegrationTest(MockMvc mockMvc, ContactService contactService){
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.contactService = contactService;
    }

    @Test
    public void testThatCreateContactSuccessfullyReturnsHttp201() throws Exception {
        ContactDto testContact1 = TestDataUtils.createTestContactDto(null);

        String createContactJson = objectMapper.writeValueAsString(testContact1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createContactJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateContactReturnsCreatedContact() throws Exception {
        ContactDto testContact1 = TestDataUtils.createTestContactDto(null);

        String createContactJson = objectMapper.writeValueAsString(testContact1);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/contact")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createContactJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value("test@test.com")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("Contact A")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value("99 99999-9999")
        );
    }

    @Test
    public void testThatListContactsReturnsHttp200() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/contact/all/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListContactReturnsContacts() throws Exception {
        ContactModel testContactModel1 = TestDataUtils.createTestContactModel1(null);
        contactService.createContact(testContactModel1);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/contact/all/1")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].name").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].email").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].phone").isString()
        );
    }

    @Test
    public void testThatGetContactReturnsHttp200WhenContactExists() throws Exception {
        ContactModel contact = TestDataUtils.createTestContactModel1(null);
        contactService.createContact(contact);

        List<ContactModel> contactList = contactService.findAllContacts();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/contact/{id}", contactList.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testThatGetContactReturnsHttp404WhenContactDoesNotExists() throws Exception {
        final UUID id = UUID.randomUUID();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/contact/{id}",id)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testThatGetUserReturnsContactWhenExist() throws Exception {
        ContactModel contact = TestDataUtils.createTestContactModel1(null);
        contactService.createContact(contact);

        List<ContactModel> contactList = contactService.findAllContacts();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/contact/" + contactList.get(1).getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(contactList.get(1).getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(contactList.get(1).getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(contactList.get(1).getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value(contactList.get(1).getPhone())
        );
    }

    @Test
    public void testThatUpdateContactReturnsUpdatedContact() throws Exception {
        ContactModel testContactModel = TestDataUtils.createTestContactModel1(null);
        ContactModel savedContactModel = contactService.updateContact(testContactModel);

        ContactDto contactDto = TestDataUtils.createTestContactDto(null);
        contactDto.setId(savedContactModel.getId());
        String contactJson = objectMapper.writeValueAsString(contactDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/contact/" + savedContactModel.getId() )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(savedContactModel.getId().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.phone").value(savedContactModel.getPhone())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(savedContactModel.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(savedContactModel.getName())
        );
    }


    @Test
    public void testThatUpdateContactReturnsHttp201WhenContactDoesNotExists() throws Exception {
        ContactModel testContactModel = TestDataUtils.createTestContactModel1(null);
        ContactModel savedContactModel = contactService.updateContact(testContactModel);

        ContactDto contactDto = TestDataUtils.createTestContactDto(null);
        contactDto.setId(savedContactModel.getId());
        String contactJson = objectMapper.writeValueAsString(contactDto);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/contact/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contactJson)
        ).andExpect(MockMvcResultMatchers.status().isCreated());
    }


}
