package taskmanagement.dto;

public record CommentResponse(
        String id,
        String task_id,
        String text,
        String author
) {
}
