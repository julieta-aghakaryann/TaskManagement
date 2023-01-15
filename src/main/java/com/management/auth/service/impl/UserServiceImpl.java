package com.management.auth.service.impl;

import com.management.auth.entity.Role;
import com.management.auth.entity.Task;
import com.management.auth.entity.User;
import com.management.auth.repository.TaskRepository;
import com.management.auth.repository.UserRepository;
import com.management.auth.service.UserService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public UserServiceImpl(UserRepository userRepository,
                           TaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(new User());
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("Username [%s] not found", user.getUsername()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toList());
    }

    public User findByUsername(@NotNull String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<Task> getUserTasks(Long userId){
        User user = userRepository.findById(userId).orElse(new User());
        return taskRepository.findAllByUser(user);
    }
}