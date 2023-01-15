package com.management.auth.service.impl;

import com.management.auth.dto.TaskDto;
import com.management.auth.entity.Task;
import com.management.auth.entity.TaskStatus;
import com.management.auth.entity.User;
import com.management.auth.repository.TaskRepository;
import com.management.auth.repository.UserRepository;
import com.management.auth.service.TaskService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public TaskServiceImpl(TaskRepository taskRepository,
                           UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    @Transactional
    public Task createTask(TaskDto dto){
        Task task = modelMapper.map(dto, Task.class);
        task.setStatus(TaskStatus.NEW_TASK);
        return taskRepository.save(task);
    }

    @Override
    public Task getTask(Long taskId){
        Task task = taskRepository.findById(taskId).orElse(new Task());
        if(!task.getVisibility()){
            throw new RuntimeException("No such task exists");
        }
        return task;
    }

    @Override
    public List<Task> getAll(){
        List<Task> all = taskRepository.findAll();
        return isVisible(all);
    }

    @Override
    @Transactional
    public Task assignTaskToUser(Long taskId, Long userid){
        Task task = taskRepository.findById(taskId).orElse(new Task());
        User user = userRepository.findById(userid).orElse(new User());
        task.setUser(user);
        return task;
    }

    @Override
    @Transactional
    public Task updateStatus(Long taskId, TaskStatus status){
        Task task = taskRepository.findById(taskId).orElse(new Task());
        task.setStatus(status);
        return task;
    }

    @Override
    public List<Task> getTaskByStatus(TaskStatus taskStatus){
        return isVisible(taskRepository.findAllByStatus(taskStatus));
    }

    @Override
    public List<Task> getTasksByUser(Long userId){
        User user = userRepository.findById(userId).orElse(new User());
        return isVisible(taskRepository.findAllByUser(user));
    }

    @Override
    public List<Task> getTasksByCreationDate(LocalDate date){
        return isVisible(taskRepository.findAllByCreatedAt(date));
    }

    @Override
    @Transactional
    public void deleteTask(Long taskId){
        Task task = taskRepository.findById(taskId).orElse(new Task());
        task.setVisibility(false);
    }

    @Override
    public List<Task> isVisible(List<Task> tasks){
        List<Task> visibleTasks = new ArrayList<>();
        for (Task task: tasks) {
            if(task.getVisibility()){
                visibleTasks.add(task);
            }
        }
        return visibleTasks;
    }
}