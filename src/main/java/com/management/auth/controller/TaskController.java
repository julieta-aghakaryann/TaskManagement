package com.management.auth.controller;

import com.management.auth.dto.TaskDto;
import com.management.auth.entity.TaskStatus;
import com.management.auth.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("api/v1/admin/task")
public class TaskController {

    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @PostMapping("/create")
    public ResponseEntity<?> createTask(@RequestBody TaskDto dto) {
        return ResponseEntity.ok().body(taskService.createTask(dto));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/get/{taskId}")
    public ResponseEntity<?> getTask(@PathVariable("taskId") Long taskId) {
        return ResponseEntity.ok().body(taskService.getTask(taskId));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/getAll")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok().body(taskService.getAll());
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/assign")
    public ResponseEntity<?> assignTaskToUser(@RequestParam Long taskId, @RequestParam Long userId) {
        return ResponseEntity.ok().body(taskService.assignTaskToUser(taskId, userId));
    }

    @GetMapping("/updateStatus")
    public ResponseEntity<?> updateStatus(@RequestParam Long taskId, @RequestParam TaskStatus status) {
        return ResponseEntity.ok().body(taskService.updateStatus(taskId, status));
    }

    @GetMapping("/getTaskByStatus/{taskStatus}")
    public ResponseEntity<?> getTaskByStatus(@PathVariable("taskStatus") TaskStatus taskStatus) {
        return ResponseEntity.ok().body(taskService.getTaskByStatus(taskStatus));
    }

    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/getTaskByUser/{userId}")
    public ResponseEntity<?> getTaskByUser(@PathVariable("userId") Long userId) {
        return ResponseEntity.ok().body(taskService.getTasksByUser(userId));
    }

    @GetMapping("/getTaskByCreationDate")
    public ResponseEntity<?> getTaskByCreationDate(@RequestBody LocalDate date) {
        return ResponseEntity.ok().body(taskService.getTasksByCreationDate(date));
    }
}
