package ru.bratushkadan.bratushkadan.controller;

import ru.bratushkadan.bratushkadan.model.Phone;
import ru.bratushkadan.bratushkadan.repository.PhoneRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phone")
public class PhoneController {
  @Autowired
  private PhoneRepository telephoneRepository;

  @GetMapping("/all")
  public List<Phone> getTelephones() {
    return telephoneRepository.findAll();
  }

  @GetMapping("/{id}")
  public Phone getTelephone(@PathVariable Long id) {
    return telephoneRepository.findById(id).orElse(null);
  }

  @PostMapping("/")
  public Phone addTelephone(@RequestBody Phone telephone) {
    return telephoneRepository.save(telephone);
  }

  @PutMapping("/")
  public Phone updateTelephone(@RequestBody Phone telephone) {
    return telephoneRepository.save(telephone);
  }

  @DeleteMapping("/{id}")
  public void deleteTelephone(@PathVariable Long id) {
    telephoneRepository.deleteById(id);
  }
}
