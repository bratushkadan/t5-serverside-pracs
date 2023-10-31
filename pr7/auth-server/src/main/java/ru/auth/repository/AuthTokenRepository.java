package ru.auth.repository;

import ru.auth.model.AuthToken;

import org.springframework.stereotype.Repository;


import org.springframework.data.repository.CrudRepository;

@Repository
public interface AuthTokenRepository extends CrudRepository<AuthToken, String> {
}
