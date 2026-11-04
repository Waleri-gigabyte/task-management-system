package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import taskmanagement.dto.*;
import taskmanagement.entity.Comment;
import taskmanagement.entity.Task;
import taskmanagement.mapper.TaskMapper;
import taskmanagement.repository.CommentRepository;
import taskmanagement.service.CommentService;
import taskmanagement.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {

    private final TaskService service;
    private final TaskMapper mapper;
    private final CommentService commentService;

    public TasksController (
            TaskService service,
            TaskMapper mapper,
            CommentService commentService
    ){
        this.service = service;
        this.mapper = mapper;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<TaskResponse> add (
            @Valid @RequestBody TaskRequest request
            ){
        Task task = service.save(request);
        TaskResponse response = mapper.toResponse(task);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Void> postComment (
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request
    ){
        commentService.addComment(taskId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskWithCommentsResponse>> get (
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String assignee
    ){
        List<TaskWithCommentsResponse> tasks = service.getAll(author, assignee);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> getAllComments (
            @PathVariable Long taskId
    ){
        List<CommentResponse> comments = commentService.getAllComments(taskId);
        return ResponseEntity.ok(comments);
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
