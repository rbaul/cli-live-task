package com.github.rbaul.clilivetask.backend.services.impl;

import com.github.rbaul.clilivetask.backend.domain.model.Task;
import com.github.rbaul.clilivetask.backend.domain.model.types.TaskState;
import com.github.rbaul.clilivetask.backend.services.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Service
public class CliExecutionServiceImpl {

    @Value("${cli-live-task.log-folder}")
    private String logsFolder;

    @Lazy
    @Autowired
    private TaskMessageServiceImpl taskMessageService;

    private final TaskAssistanceServiceImpl taskAssistanceService;

    @Lazy
    @Autowired
    private TaskService taskService;

    public String getLogFilePath(long taskId) {
        return logsFolder + "/" + taskId + ".log";
    }

    @Async
    @Transactional(readOnly = true)
    public void executeCliCommand(long taskId) {
        Path rootFileStorageLocation = Paths.get(logsFolder)
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(rootFileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }

        Path resolve = rootFileStorageLocation.resolve(taskId + ".log");
//        folderPath = Paths.get(getLogFilePath(taskId))
//                .toAbsolutePath().normalize();

        Task taskPersisted = taskAssistanceService.getTaskById(taskId);
        String command = taskPersisted.getCommand();

        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;

        try {
            String fileName = getLogFilePath(taskId);
            File file = new File(fileName);
            if (!file.exists()) {
                Files.createFile(resolve);
            }
            fileOutputStream = new FileOutputStream(fileName, false);

            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            ProcessBuilder builder = new ProcessBuilder();
            if (isWindows) {
                builder.command("cmd.exe", "/c", command);
            } else {
                builder.command("sh", "-c", command);
            }
            builder.redirectErrorStream(true);
            builder.directory(new File(System.getProperty("user.home")));

            taskService.updateTaskState(taskId, TaskState.EXECUTING);

            Process process = builder.start();

            inputStream = process.getInputStream();

            WriteFileListenerImpl writeFileListener = new WriteFileListenerImpl(taskId,taskMessageService);
            String read = ConnectionStreamUtils.read(inputStream, fileOutputStream, writeFileListener);
            taskService.updateTaskState(taskId, TaskState.DONE);
        } catch (IOException e) {
            taskService.updateTaskState(taskId, TaskState.FAILED);
            log.error("Error", e);
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) { }
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) { }
        }
    }
}
