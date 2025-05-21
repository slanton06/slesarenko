package com.taskmanager.taskmanager.service;

import com.taskmanager.taskmanager.model.Task;
import com.taskmanager.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TaskService {
    @Autowired private TaskRepository repo;

    public List<Task> getAllTasks() { return repo.findAll(); }

    public void addTask(Task task) { repo.save(task); }
    public void updateTask(Task task) { repo.save(task); }
    public void deleteTask(Task task) { repo.delete(task); }
}
