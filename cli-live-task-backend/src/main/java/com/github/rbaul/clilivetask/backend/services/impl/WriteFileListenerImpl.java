package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.services.WriteFileListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class WriteFileListenerImpl implements WriteFileListener {
    private final long taskId;

    private final TaskMessageServiceImpl taskMessageService;

    @Override
    public void writeFileUpdate(String progressLog) {
        taskMessageService.notifyProgressLogTaskUpdate(taskId, progressLog);
    }
}
