package com.taskmanager.taskmanager.controller;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() { return taskService.getAllTasks(); }

    @PostMapping
    public void addTask(@RequestBody Task task) { taskService.addTask(task); }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(taskService.getAllTasks().stream()
                .filter(task -> task.getId().equals(id))
                .findFirst().orElse(null));
    }
}
