package com.management.auth.repository;

import com.management.auth.entity.Task;
import com.management.auth.entity.TaskStatus;
import com.management.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByUser(User user);
    List<Task> findAllByStatus(TaskStatus taskStatus);
    List<Task> findAllByCreatedAt(LocalDate createdAt);
}
