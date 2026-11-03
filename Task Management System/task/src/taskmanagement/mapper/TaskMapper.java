package taskmanagement.mapper;

import org.springframework.stereotype.Component;
import taskmanagement.dto.TaskResponse;
import taskmanagement.entity.Task;

@Component
public class TaskMapper {

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId().toString(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAuthor().getEmail(),
                task.getAssignee() == null
                        ? "none"
                        : task.getAssignee().getEmail()
        );
    }
}