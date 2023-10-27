package ru.bratushkadan.bratushkadan.model;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String email;
    
    private String login;
    
    private String password;
}