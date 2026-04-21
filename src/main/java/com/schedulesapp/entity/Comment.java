package com.schedulesapp.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String content;

    // @ManyToOne: 다대일(N:1) 관계
    // fetch = FetchType.LAZY: 데이터베이스에서 댓글을 가져올 때,
    // 연결된 일정(Schedule) 정보는 당장 가져오지 않고 '가짜 객체(프록시)'만 채워둔다
    // optional = false: 이 댓글 객체는 반드시 Schedule 객체를 가지고 있어야 한다
    // @JoinColumn(name = "scheduleId"): 외래키 이름 지정 (안하면 JPA가 지맘대로 복잡한 이름 지음)
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scheduleId")
    private Schedule schedule;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId")
    private User user;

    public Comment(String content, Schedule schedule, User user) {
        this.content = content;
        this.schedule = schedule;
        this.user = user;
    }

}
