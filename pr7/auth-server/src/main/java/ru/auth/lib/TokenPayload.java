package ru.auth.lib;

import java.io.Serializable;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.auth.constant.Role;

@Data
@AllArgsConstructor
public class TokenPayload implements Serializable {
  private String username;
  private Role role;

  public String toJson() {
    return new Gson().toJson(this);
  }
}
