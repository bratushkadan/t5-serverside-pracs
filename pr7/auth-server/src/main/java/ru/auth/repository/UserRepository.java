package ru.auth.repository;

import ru.auth.model.User;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
}
