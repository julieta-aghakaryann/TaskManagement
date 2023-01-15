package com.management.auth.service;

import com.management.auth.entity.Task;
import com.management.auth.entity.User;

import java.util.List;

public interface UserService{
    User getUserById(Long userId);
    User getUserByUsername(String username);
    List<Task> getUserTasks(Long userId);
}