package com.example.springboot.repositories;

import com.example.springboot.models.ContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, UUID>,
        PagingAndSortingRepository<ContactModel, UUID> {
}
