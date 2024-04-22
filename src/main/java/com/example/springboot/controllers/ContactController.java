package com.example.springboot.controllers;


import com.example.springboot.mappers.Mapper;
import com.example.springboot.models.ContactModel;
import com.example.springboot.models.dtos.ContactDto;
import com.example.springboot.service.ContactService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ContactController {

    private final Mapper<ContactModel, ContactDto> contactMapper;

    private final ContactService contactService;

    public ContactController(Mapper<ContactModel, ContactDto> contactMapper, ContactService contactService) {
        this.contactMapper = contactMapper;
        this.contactService = contactService;
    }


    @PostMapping("/contact")
    public ResponseEntity<ContactDto> registerContact(@RequestBody @Valid ContactDto contactDto){
       ContactModel contactModel = contactMapper.mapFrom(contactDto);
       ContactModel savedContactModel = contactService.createContact(contactModel);
       ContactDto savedContactDto = contactMapper.mapTo(savedContactModel);
       return new ResponseEntity<>(savedContactDto, HttpStatus.CREATED);
    }

    @GetMapping("/contact/all/{userId}")
    public List<ContactDto> getAllContacts(@PathVariable String userId){
        List<ContactModel> contactList = contactService.findAllContacts();
        return contactList.stream().map(contactMapper::mapTo).collect(Collectors.toList());
    }

    @GetMapping("/contact/page")
    public Page<ContactDto> getAllContactsPage(Pageable pageable){
        Page<ContactModel> contactPage = contactService.findAll(pageable);
        return contactPage.map(contactMapper::mapTo);
    }

    @GetMapping("/contact/user/{userId}")
    public List<ContactDto> getAllContactsFromUser(@PathVariable String userId){
        List<ContactModel> contactList = contactService.findByUserId(userId);
        return contactList.stream().map(contactMapper::mapTo).collect(Collectors.toList());
    }


    @GetMapping("/contact/{id}")
    public ResponseEntity<ContactDto> getOneContact(@PathVariable("id") String id){
        Optional<ContactModel> contact = contactService.findOne(id);
        return contact.map(contactModel -> {
            ContactDto contactDto = contactMapper.mapTo(contactModel);
            return new ResponseEntity<>(contactDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/contact/{id}")
    public ResponseEntity<ContactDto> createUpdateContact(@PathVariable("id") String id, @RequestBody @Valid ContactDto contactDto) {
        ContactModel contactModel = contactMapper.mapFrom(contactDto);
        boolean contactExists = contactService.isExists(contactService.convertIdToUUID(id));
        ContactModel savedContact = contactService.updateContact(contactModel);
        ContactDto savedUpdatedContactDto = contactMapper.mapTo(savedContact);

        if(contactExists){
            return new ResponseEntity<>(savedUpdatedContactDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(savedUpdatedContactDto, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(path = "/contact/{id}")
    public ResponseEntity deleteContact(@PathVariable("id") String id){
        contactService.delete(contactService.convertIdToUUID(id));
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
