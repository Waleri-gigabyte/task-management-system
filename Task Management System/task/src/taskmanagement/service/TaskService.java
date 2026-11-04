package taskmanagement.service;


import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.dto.*;
import taskmanagement.entity.Comment;
import taskmanagement.entity.Task;
import taskmanagement.entity.TaskStatus;
import taskmanagement.entity.UserAccount;
import taskmanagement.mapper.TaskMapper;
import taskmanagement.repository.CommentRepository;
import taskmanagement.repository.TasksRepository;
import taskmanagement.repository.UsersRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TaskService {

    private final TasksRepository repository;
    private final UsersRepository usersRepository;
    private final CommentRepository commentRepository;
    private final TaskMapper mapper;


    public TaskService(
            TasksRepository repository,
            UsersRepository usersRepository,
            CommentRepository commentRepository,
            TaskMapper mapper
    ){
        this.repository = repository;
        this.usersRepository = usersRepository;
        this.commentRepository = commentRepository;
        this.mapper = mapper;
    }

    public Task save (TaskRequest request){

        Task task = new Task();

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(TaskStatus.CREATED);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.valueOf(authentication.getName());

        UserAccount author = usersRepository
                .findById(userId)
                .orElseThrow();

        task.setAuthor(author);

        Task savedTask = repository.save(task);

        return savedTask;
    }

    public List<TaskWithCommentsResponse> getAll (
            String author,
            String assignee
    ){
        List<Task> tasks = repository.findFilteredTasks(author, assignee);
        List<Long> taskIds = tasks.stream()
                .map(Task::getId)
                .toList();

        List<Object[]> results = taskIds.isEmpty()
                ? List.of()
                : commentRepository.countCommentsByTaskIds(taskIds);

        Map<Long, Long> commentCounts = new HashMap<>();

        for (Object[] row : results) {
            Long taskId = (Long) row[0];
            Long count = (Long) row[1];

            commentCounts.put(taskId, count);
        }

        return tasks.stream()
                .map(task -> {
                    long count = commentCounts.getOrDefault(task.getId(), 0L);

                    return mapper.toResponseWithComments(
                            task,
                            Math.toIntExact(count)
                    );
                })
                .toList();
    }

    public TaskResponse assignTask (
            Long taskId,
            AssignTaskRequest request
    ){

        Task task = repository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found"
                ));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.valueOf(authentication.getName());

        if (!task.getAuthor().getId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        String assigneeEmail = request.assignee();

        if ("none".equals(assigneeEmail)) {
            task.setAssignee(null);
        } else {
            UserAccount assignee = usersRepository
                    .findByEmailIgnoreCase(assigneeEmail)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Assignee not found"
                    ));

            task.setAssignee(assignee);
        }

        Task savedTask = repository.save(task);

        return mapper.toResponse(savedTask);
    }

    public TaskResponse updateStatus (
            Long taskId,
            UpdateStatusRequest request
    ){


        Task task = repository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found"
                ));

        //-----------

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.valueOf(authentication.getName());

        boolean isAuthor = task.getAuthor().getId().equals(userId);

        boolean isAssignee = task.getAssignee() != null
                && task.getAssignee().getId().equals(userId);

        if (!isAuthor && !isAssignee) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You are not allowed to update this task"
            );
        }
        //-----------

        TaskStatus newStatus;

        try {
            newStatus = TaskStatus.valueOf(request.status());
        } catch (IllegalArgumentException | NullPointerException e){
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid task status"
            );
        }

        task.setStatus((newStatus));

        Task savedTask = repository.save(task);

        return mapper.toResponse(savedTask);
    }
}
