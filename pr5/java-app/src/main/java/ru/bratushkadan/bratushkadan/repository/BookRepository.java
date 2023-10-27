package ru.bratushkadan.bratushkadan.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.bratushkadan.bratushkadan.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByAuthor(String author);
}