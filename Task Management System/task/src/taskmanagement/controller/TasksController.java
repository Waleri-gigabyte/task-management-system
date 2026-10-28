package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.TaskRequest;
import taskmanagement.dto.TaskResponse;
import taskmanagement.entity.Task;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final TaskService service;

    public TasksController (TaskService service){
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> add (
            @Valid @RequestBody TaskRequest request
            ){

        Task task = service.save(request);

        TaskResponse response = new TaskResponse(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAuthor().getEmail()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> get (
            @RequestParam(required = false) String author
    ){
        List<TaskResponse> tasks = service.getAll(author);
        return ResponseEntity.ok(tasks);
    }
}
