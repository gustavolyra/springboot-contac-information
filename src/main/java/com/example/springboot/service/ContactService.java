package com.example.springboot.service;

import com.example.springboot.models.ContactModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ContactService {

    ContactModel createContact(ContactModel contactModel);

    ContactModel updateContact(ContactModel contactModel);

    List<ContactModel> findAllContacts();

    Page<ContactModel> findAll(Pageable pageable);

    Optional<ContactModel> findOne(String id);

    boolean isExists(UUID id);

    UUID convertIdToUUID(String id);

    void delete(UUID id);
}
