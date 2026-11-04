package taskmanagement.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.dto.CommentRequest;
import taskmanagement.dto.CommentResponse;
import taskmanagement.entity.Comment;
import taskmanagement.entity.Task;
import taskmanagement.entity.UserAccount;
import taskmanagement.mapper.CommentMapper;
import taskmanagement.repository.CommentRepository;
import taskmanagement.repository.TasksRepository;
import taskmanagement.repository.UsersRepository;

import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TasksRepository tasksRepository;
    private final UsersRepository usersRepository;
    private final CommentMapper mapper;

    public CommentService (
            CommentRepository commentRepository,
            TasksRepository tasksRepository,
            UsersRepository usersRepository,
            CommentMapper mapper
    ){
        this.commentRepository = commentRepository;
        this.tasksRepository = tasksRepository;
        this.usersRepository = usersRepository;
        this.mapper = mapper;
    }

    public List<CommentResponse> getAllComments(Long taskId) {

        if (!tasksRepository.existsById(taskId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Task not found"
            );
        }

        return commentRepository.findCommentsByTaskId(taskId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void addComment (
            Long taskId,
            CommentRequest request
    ){
        Task task = tasksRepository.findById(taskId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Task not found"
                ));

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        Long userId = Long.valueOf(authentication.getName());

        UserAccount author = usersRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        Comment comment = new Comment();

        comment.setTask(task);
        comment.setAuthor(author);
        comment.setText(request.text());

        commentRepository.save(comment);

    }
}
