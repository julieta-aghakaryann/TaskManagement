package com.management.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {
    @NotNull
    private String taskName;
    @NotNull
    private LocalDate createdAt;
    @NotNull
    private LocalDate updatedAt;
    @NotNull
    private Boolean visibility;
    @NotNull
    private String description;
    @NotNull
    private Long user;
}
