package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.WashingMachine;

public interface WashingMachineRepository extends JpaRepository<WashingMachine, Long> {
    List<WashingMachine> findByManufacturer(String manufacturer);
    List<WashingMachine> findAll();
}
