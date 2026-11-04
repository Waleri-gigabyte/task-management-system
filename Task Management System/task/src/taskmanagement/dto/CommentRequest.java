package taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(
        @NotBlank String text
) {
}
