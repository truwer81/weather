package com.example.server.login;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
    @Id
    private Long id;
    @Column(name = "user_name")
    private String username;
    @Column(name = "user_password")
    private String password;


}
