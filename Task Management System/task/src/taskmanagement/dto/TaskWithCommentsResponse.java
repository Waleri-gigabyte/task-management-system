package taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import taskmanagement.entity.TaskStatus;

public record TaskWithCommentsResponse(
        String id,
        String title,
        String description,
        TaskStatus status,
        String author,
        String assignee,
        @JsonProperty("total_comments")
        int totalComments
) {
}
