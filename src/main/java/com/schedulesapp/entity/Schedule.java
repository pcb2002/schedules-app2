package com.schedulesapp.entity;

import com.schedulesapp.exception.CustomException;
import com.schedulesapp.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "schedules")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30, nullable = false)
    private String title;
    @Column(length = 200, nullable = false)
    private String content;
    private String author;
    private String password;

    // orphanRemoval = true: 일정이 삭제되면 연관된 댓글도 고아가 되어 함께 삭제됨
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    public Schedule(String title, String content, String author, String password) {
        this.title = title;
        this.content = content;
        this.author = author;
        this.password = password;
    }

    public void update(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public void checkPassword(String inputPassword) {
        if (inputPassword == null || inputPassword.trim().isEmpty()) {
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
        }
        if (!this.password.equals(inputPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH);
        }
    }
}
