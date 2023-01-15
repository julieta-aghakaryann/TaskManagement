package com.management.auth.service;

import com.management.auth.dto.TaskDto;
import com.management.auth.entity.Task;
import com.management.auth.entity.TaskStatus;

import java.time.LocalDate;
import java.util.List;
public interface TaskService {
    Task createTask(TaskDto dto);
    Task getTask(Long taskId);
    List<Task> getAll();
    Task assignTaskToUser(Long taskId, Long userid);
    Task updateStatus(Long taskId, TaskStatus status);
    List<Task> getTaskByStatus(TaskStatus taskStatus);
    List<Task> getTasksByUser(Long userId);
    List<Task> getTasksByCreationDate(LocalDate date);
    void deleteTask(Long taskId);
    List<Task> isVisible(List<Task> tasks);
}
