package ru.auth.model;


import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RedisHash("AuthToken")
@Data
@RequiredArgsConstructor
public class AuthToken implements Serializable {

  @Id
  private final String username;
  private final String token;
}
