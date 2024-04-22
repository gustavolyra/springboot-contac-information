package com.example.springboot.service.impl;

import com.example.springboot.models.ContactModel;
import com.example.springboot.repositories.ContactRepository;
import com.example.springboot.service.ContactService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @Override
    public void delete(UUID id){
        contactRepository.deleteById(id);
    }

    @Override
    public ContactModel createContact(ContactModel contactModel) {
        return contactRepository.save(contactModel);
    }

    @Override
    public ContactModel updateContact(ContactModel contactModel) {
        return contactRepository.save(contactModel);
    }

    public List<ContactModel> findAllContacts() {
        return StreamSupport.stream(contactRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ContactModel> findAll(Pageable pageable) {
        return contactRepository.findAll(pageable);
    }

    @Override
    public Optional<ContactModel> findOne(String id){
        return contactRepository.findById(convertIdToUUID(id));
    }

    @Override
    public List<ContactModel> findByUserId(String user_id){
        return  contactRepository.findByUserId(convertIdToUUID(user_id));
    }

    @Override
    public boolean isExists(UUID id) {
        return contactRepository.existsById(id);
    }

    @Override
    public UUID convertIdToUUID(String id) {
        return UUID.fromString(id);
    }
}
