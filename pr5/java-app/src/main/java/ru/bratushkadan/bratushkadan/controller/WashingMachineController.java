package ru.bratushkadan.bratushkadan.controller;

import ru.bratushkadan.bratushkadan.model.WashingMachine;
import ru.bratushkadan.bratushkadan.repository.WashingMachineRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/washingMachine")
public class WashingMachineController {
  @Autowired
  private WashingMachineRepository washingMachineRepository;

    @GetMapping("/all")
  public List<WashingMachine> getWashingMachines() {
    return washingMachineRepository.findAll();
  }

  @GetMapping("/{id}")
  public WashingMachine getWashingMachine(@PathVariable Long id) {
    return washingMachineRepository.findById(id).orElse(null);
  }

  @PostMapping("/")
  public WashingMachine addWashingMachine(@RequestBody WashingMachine washingMachine) {
    return washingMachineRepository.save(washingMachine);
  }

  @PutMapping("/")
  public WashingMachine updateWashingMachine(@RequestBody WashingMachine washingMachine) {
    return washingMachineRepository.save(washingMachine);
  }

  @DeleteMapping("/{id}")
  public void deleteWashingMachine(@PathVariable Long id) {
    washingMachineRepository.deleteById(id);
  }
}