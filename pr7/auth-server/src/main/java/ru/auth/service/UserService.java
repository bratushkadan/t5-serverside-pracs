package ru.auth.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ru.auth.constant.Role;
import ru.auth.model.User;
import ru.auth.repository.UserRepository;

@Service
public class UserService {
  @Autowired
  private UserRepository userRepository;

  public static class UserExistsError extends Error {
  }

  public List<User> getAll() {
    return StreamSupport.stream(userRepository.findAll().spliterator(), false).collect(Collectors.toList());
  }

  public User getUser(String username) {
    Optional<User> usr = userRepository.findById(username);
    if (!usr.isPresent()) {
      return null;
    }
    return usr.get();
  }

  public void updateUser(User user) {
    userRepository.save(user);
  }

  public void deleteAll() {
    userRepository.deleteAll();
  }

  public void createUser(String username, String password, Role role) throws UserExistsError {
    User user = getUser(username);
    if (user != null) {
      throw new UserService.UserExistsError();
    }

    user = new User(username);
    user.setPassword(password);
    user.setRole(role);

    userRepository.save(user);
  }
}
