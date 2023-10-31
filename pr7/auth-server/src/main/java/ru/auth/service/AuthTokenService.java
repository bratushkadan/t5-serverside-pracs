package ru.auth.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;

import ru.auth.lib.Token;
import ru.auth.lib.TokenPayload;
import ru.auth.model.AuthToken;
import ru.auth.model.User;
import ru.auth.repository.AuthTokenRepository;
import ru.auth.repository.UserRepository;

@Service
public class AuthTokenService {
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private AuthTokenRepository authTokenRepository;

  public class UserNotExistsError extends Error {
  }

  public class WrongCredentialsError extends Error {
  }

  public String getToken(String username, String password)
      throws UserNotExistsError, WrongCredentialsError, JWTCreationException {
    Optional<User> usr = userRepository.findById(username);
    if (!usr.isPresent()) {
      throw new UserNotExistsError();
    }
    User user = usr.get();
    if (!user.getPassword().equals(password)) {
      throw new WrongCredentialsError();
    }

    Optional<AuthToken> t = authTokenRepository.findById(user.getUsername());
    if (t.isPresent()) {
      return t.get().getToken();
    }

    String strToken = Token.create(user);

    AuthToken authToken = new AuthToken(username, strToken);
    authTokenRepository.save(authToken);

    return strToken;
  }

  public TokenPayload getTokenPayload(String token) throws JWTDecodeException {
    return Token.verify(token);
  }

  public boolean validateToken(String token) throws JWTDecodeException {
    this.getTokenPayload(token);
    return true;
  }
}
