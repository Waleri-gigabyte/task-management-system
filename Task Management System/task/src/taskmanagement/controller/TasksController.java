package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.AssignTaskRequest;
import taskmanagement.dto.TaskRequest;
import taskmanagement.dto.TaskResponse;
import taskmanagement.dto.UpdateStatusRequest;
import taskmanagement.entity.Task;
import taskmanagement.mapper.TaskMapper;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final TaskService service;
    private final TaskMapper mapper;

    public TasksController (
            TaskService service,
            TaskMapper mapper
    ){
        this.service = service;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> add (
            @Valid @RequestBody TaskRequest request
            ){
        Task task = service.save(request);
        TaskResponse response = mapper.toResponse(task);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> get (
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String assignee
    ){
        List<TaskResponse> tasks = service.getAll(author, assignee);
        return ResponseEntity.ok(tasks);
    }

    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask (
            @PathVariable Long taskId,
            @Valid @RequestBody AssignTaskRequest request
    ){
        TaskResponse response = service.assignTask(taskId, request);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateStatus(
            @PathVariable Long taskId,
            @RequestBody UpdateStatusRequest request
    ){
        TaskResponse response = service.updateStatus(taskId, request);

        return ResponseEntity.ok(response);
    }
}
