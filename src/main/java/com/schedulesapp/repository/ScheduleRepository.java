package com.schedulesapp.repository;

import com.schedulesapp.dto.SchedulePageResponse;
import com.schedulesapp.entity.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByIdAndUserId(Long id, Long userId);

    @Query("SELECT new com.schedulesapp.dto.SchedulePageResponse(" +
            "s.title, s.content, " +
            "(SELECT COUNT(c) FROM Comment c WHERE c.schedule = s), " +
            "s.createdAt, s.updatedAt, u.username) " +
            "FROM Schedule s JOIN s.user u")
    Page<SchedulePageResponse> findAllWithCommentCount(Pageable pageable);
}
