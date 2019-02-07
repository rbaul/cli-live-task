package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.domain.model.Task;
import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import com.github.rbaul.clilivetask.backend.domain.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class TaskAssistanceServiceImpl {

    private final TaskRepository taskRepository;

    @Transactional
    public Task create(Task task) {
        return taskRepository.save(task);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateState(long taskId, TaskState state) {
        Task taskById = getTaskById(taskId);
        taskById.setState(state);
    }

    @Transactional
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EmptyResultDataAccessException("No found task with id: " + taskId, 1));
    }

}
