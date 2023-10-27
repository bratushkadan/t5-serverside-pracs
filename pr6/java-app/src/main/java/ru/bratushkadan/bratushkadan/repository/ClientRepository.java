package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByEmail(String email);

    List<Client> findAll();

    void deleteById(Long id);
}
