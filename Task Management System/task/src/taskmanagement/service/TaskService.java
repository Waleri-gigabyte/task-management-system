package taskmanagement.service;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import taskmanagement.dto.TaskRequest;
import taskmanagement.dto.TaskResponse;
import taskmanagement.entity.Task;
import taskmanagement.entity.TaskStatus;
import taskmanagement.entity.UserAccount;
import taskmanagement.repository.TasksRepository;
import taskmanagement.repository.UsersRepository;

import java.util.List;

@Service
public class TaskService {

    private final TasksRepository repository;
    private final UsersRepository usersRepository;

    public TaskService(
            TasksRepository repository,
            UsersRepository usersRepository
    ){
        this.repository = repository;
        this.usersRepository = usersRepository;
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

    public List<TaskResponse> getAll (String author){
        List<Task> tasks;

        if (author == null) {
            tasks = repository.findAllByOrderByIdDesc();
        } else {
            tasks = repository.findAllByAuthor_EmailIgnoreCaseOrderByIdDesc(author);
        }

        List<TaskResponse> responses = tasks.stream()
                .map(task ->new TaskResponse(
                        task.getId().toString(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getAuthor().getEmail()
                ))
                .toList();

        return responses;
    }
}
