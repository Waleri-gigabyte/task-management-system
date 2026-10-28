package taskmanagement.dto;

import taskmanagement.entity.TaskStatus;

public record TaskResponse(
        String id,
        String title,
        String description,
        TaskStatus status,
        String author
) {
}
