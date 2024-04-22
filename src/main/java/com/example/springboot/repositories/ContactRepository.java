package com.example.springboot.repositories;

import com.example.springboot.models.ContactModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, UUID>,
        PagingAndSortingRepository<ContactModel, UUID> {

    @Query(value="Select * from tb_contact where user_id=?", nativeQuery = true)
    public List<ContactModel> findByUserId(UUID user_id);
}
