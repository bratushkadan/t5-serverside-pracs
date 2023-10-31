package ru.auth.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.auth.constant.Role;
import ru.auth.lib.Token;
import ru.auth.lib.TokenPayload;
import ru.auth.model.User;
import ru.auth.service.UserService;
import ru.auth.service.UserService.UserExistsError;

@RestController
@RequestMapping("/user")
public class UserController {
  @Autowired
  private UserService userService;

  @GetMapping("/{username}")
  public User getUserByUsername(@PathVariable("username") String username) {
    User user = userService.getUser(username);
    return user;
  }

  @GetMapping("/")
  public List<User> getUsers() {
    return userService.getAll();
  }

  @DeleteMapping("/")
  public ResponseEntity<String> deleteUsers() {
    userService.deleteAll();

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(new Gson().toJson(new UserController.HttpMessage("ok")));
  }

  @PostMapping("/")
  public ResponseEntity<String> createUser(@RequestParam String username, @RequestParam String password,
      @RequestParam(required = false) Role role) {

    if (role == null) {
      role = Role.USER;
    }

    try {
      userService.createUser(username, password, role);
      return ResponseEntity
          .status(HttpStatus.CREATED)
          .body(new Gson().toJson(new UserController.HttpMessage("ok")));
    } catch (UserExistsError _e) {
      return ResponseEntity
          .status(HttpStatus.CONFLICT)
          .body(new Gson().toJson(new UserController.HttpMessage(String.format("user %s exists", username))));
    }
  }

  @PostMapping("/change-role/{username}")
  public ResponseEntity<String> changeRole(
      @RequestHeader(name = "X-Request-API-Token", required = false) String authToken,
      @PathVariable("username") String username,
      @RequestParam(name = "role") Role role) {

    if (authToken.equals("")) {
      return ResponseEntity
          .status(HttpStatus.UNAUTHORIZED)
          .body(String.format("{\"message\": \"you're unauthorized to perform this action\"}", username));
    }

    try {
      TokenPayload tokenPayload = Token.verify(authToken);
      if (!tokenPayload.getRole().equals(Role.ADMINISTRATOR)) {
        return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .body(String.format("{\"message\": \"you have no permission to perform this action\"}", username));
      }
    } catch (JWTVerificationException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(String.format("{\"message\": \"invalid token\"}", username));
    }

    User user = userService.getUser(username);

    if (user == null) {
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(String.format("{\"message\": \"user %s not found\"}", username));
    }

    user.setRole(role);
    userService.updateUser(user);

    return ResponseEntity
        .status(HttpStatus.OK)
        .body(String.format("{\"message\": \"ok\"}", user.getUsername()));
  }

  @Data
  @AllArgsConstructor
  private class HttpMessage {
    private String message;
  }
}
