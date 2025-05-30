package com.toms.app.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    private String email;
    private String role;
    private String phone;

    public User(String username, String password, String email, String role, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.phone = phone;
    }
}