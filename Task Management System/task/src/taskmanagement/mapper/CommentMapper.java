package taskmanagement.mapper;

import org.springframework.stereotype.Component;
import taskmanagement.dto.CommentResponse;
import taskmanagement.entity.Comment;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId().toString(),
                comment.getTask().getId().toString(),
                comment.getText(),
                comment.getAuthor().getEmail()
        );
    }
}

