package ru.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.google.gson.Gson;

import lombok.Data;
import ru.auth.lib.TokenPayload;
import ru.auth.service.AuthTokenService;
import ru.auth.service.AuthTokenService.UserNotExistsError;
import ru.auth.service.AuthTokenService.WrongCredentialsError;

@RequestMapping("/token")
@RestController
public class AuthTokenController {
  @Autowired
  AuthTokenService authTokenService;

  @GetMapping("/")
  public ResponseEntity<String> get(@RequestParam String username, @RequestParam String password) {
    try {
      String token = authTokenService.getToken(username, password);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(new TokenHttpResponse(token).toJson());
    } catch (UserNotExistsError e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new MessageHttpResponse(String.format("user \"%s\" doesn't exist", username)).toJson());
    } catch (WrongCredentialsError e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new MessageHttpResponse("wrong credentials").toJson());
    } catch (JWTCreationException e) {
      return ResponseEntity
          .status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new MessageHttpResponse("error creating token").toJson());
    }
  }

  @PostMapping("/payload")
  public ResponseEntity<String> validateAndGetPayload(@RequestHeader(name = "X-Request-API-Token") String strToken) {

    try {
      TokenPayload tokenPayload = authTokenService.getTokenPayload(strToken);
      return ResponseEntity
          .status(HttpStatus.OK)
          .body(tokenPayload.toJson());
    } catch (JWTDecodeException e) {
      return ResponseEntity
          .status(HttpStatus.BAD_REQUEST)
          .body(new MessageHttpResponse("token is invalid").toJson());
    }
  }

  @Data
  private static class TokenHttpResponse {
    private final String token;

    public String toJson() {
      return new Gson().toJson(this);
    }
  }

  @Data
  private static class MessageHttpResponse {
    private final String message;

    public String toJson() {
      return new Gson().toJson(this);
    }
  }
}
