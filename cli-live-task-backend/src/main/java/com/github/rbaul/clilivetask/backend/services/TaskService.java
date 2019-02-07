package com.github.rbaul.clilivetask.backend.services;

import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import com.github.rbaul.clilivetask.backend.web.dtos.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {
    TaskDto create(TaskDto taskDto);
    TaskDto update(Long taskId, TaskDto taskDto);
    TaskDto get(Long taskId);
    void delete(Long taskId);

    List<TaskDto> getAll();
    Page<TaskDto> getPageable(Pageable pageable);
    String getLogFileById(Long taskId);
    void updateTaskState(long taskId, TaskState state);
}
