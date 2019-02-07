package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.domain.model.Task;
import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import com.github.rbaul.clilivetask.backend.domain.repository.TaskRepository;
import com.github.rbaul.clilivetask.backend.services.TaskService;
import com.github.rbaul.clilivetask.backend.services.WriteFileListener;
import com.github.rbaul.clilivetask.backend.web.dtos.TaskDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    private final ModelMapper modelMapper;

    private final CliExecutionServiceImpl cliExecutionService;

    private final TaskAssistanceServiceImpl taskAssistanceService;

    private final TaskMessageServiceImpl taskMessageService;

    @Override
    public TaskDto create(TaskDto taskDto) {
        Task task = taskAssistanceService.create(modelMapper.map(taskDto, Task.class));
        cliExecutionService.executeCliCommand(task.getId());
        return modelMapper.map(task, TaskDto.class);
    }

    @Transactional
    @Override
    public TaskDto update(Long taskId, TaskDto taskDto) {
        Task task = taskAssistanceService.getTaskById(taskId);
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setCommand(taskDto.getCommand());
        return modelMapper.map(task, TaskDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public TaskDto get(Long taskId) {
        Task task = taskAssistanceService.getTaskById(taskId);
        return modelMapper.map(task, TaskDto.class);
    }

    @Transactional
    @Override
    public void delete(Long taskId) {
        if (!taskRepository.existsById(taskId)){
            throw new EmptyResultDataAccessException("No found task with id: " + taskId, 1);
        }
        taskRepository.deleteById(taskId);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskDto> getAll() {
        return taskRepository.findAll().stream()
                .map(task -> modelMapper.map(task, TaskDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void updateTaskState(long taskId, TaskState state) {
        taskAssistanceService.updateState(taskId, state);
        taskMessageService.notifyTasksUpdate();
    }

    @Override
    public Page<TaskDto> getPageable(Pageable pageable) {
        return taskRepository.findAll(pageable)
                .map(task -> modelMapper.map(task, TaskDto.class));
    }

    @Override
    public String getLogFileById(Long taskId) {
        String logFilePath = cliExecutionService.getLogFilePath(taskId);
        Path path = Paths.get(logFilePath);
        StringBuilder builder = new StringBuilder();
        try {
            Files.readAllLines(path).forEach(line -> {
                builder.append(line);
                builder.append("\n");
            });
        } catch (IOException e) {

        }

        return builder.toString();
    }

}
