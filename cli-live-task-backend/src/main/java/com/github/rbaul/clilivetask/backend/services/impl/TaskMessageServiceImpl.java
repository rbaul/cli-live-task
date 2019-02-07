package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class TaskMessageServiceImpl {

    private final SimpMessagingTemplate template;

    @Autowired
    @Lazy
    private TaskService taskService;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void notifyLogTaskUpdate(Long taskId) {
        template.convertAndSend("/topic/task-log-update/" + taskId, taskService.getLogFileById(taskId));
    }

    public void notifyProgressLogTaskUpdate(Long taskId, String progressLog) {
        template.convertAndSend("/topic/task-progress-log-update/" + taskId, progressLog);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void notifyTasksUpdate() {
        template.convertAndSend("/topic/tasks-update", taskService.getAll());
    }
}
