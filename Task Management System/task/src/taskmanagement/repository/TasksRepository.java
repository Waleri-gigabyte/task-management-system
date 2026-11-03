package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskmanagement.entity.Task;

import java.util.List;

public interface TasksRepository extends JpaRepository <Task, Long> {

    List<Task> findAllByOrderByIdDesc();

    List<Task> findAllByAuthor_EmailIgnoreCaseOrderByIdDesc(String email);

    @Query("""
        SELECT t FROM Task t
        LEFT JOIN t.assignee a
        WHERE (:author IS NULL OR LOWER(t.author.email) = LOWER(:author))
          AND (:assignee IS NULL OR LOWER(a.email) = LOWER(:assignee))
        ORDER BY t.id DESC
    """)
    List<Task> findFilteredTasks(
            @Param("author") String author,
            @Param("assignee") String assignee
    );
}
