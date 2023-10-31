package ru.auth.model;

import ru.auth.constant.Role;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RedisHash("User")
@Data
@RequiredArgsConstructor
public class User implements Serializable {

  @Id
  private final String username;
  private String password;
  private Role role;
}
