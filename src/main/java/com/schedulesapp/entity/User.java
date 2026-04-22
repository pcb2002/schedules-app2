package com.schedulesapp.entity;

import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 4, nullable = false)
    private String username;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void update(String username, String email) {
        this.username = username;
        this.email = email;
    }

}
