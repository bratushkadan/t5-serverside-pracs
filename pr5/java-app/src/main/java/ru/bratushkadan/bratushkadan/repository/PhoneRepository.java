package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Long> {
    List<Phone> findByManufacturer(String manufacturer);
    List<Phone> findAll();
}

