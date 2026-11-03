package taskmanagement.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AssignTaskRequest(
        @NotNull
        @Pattern(
                regexp = "none|[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}",
                message = "Assignee must be a valid email or 'none'"
        )
        String assignee
) {
}
