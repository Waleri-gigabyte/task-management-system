package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskmanagement.entity.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("""
       SELECT c
       FROM Comment c
       WHERE c.task.id = :taskId
       ORDER BY c.id DESC
       """)
    List<Comment> findCommentsByTaskId(
            @Param("taskId") Long taskId
    );

    @Query("""
    SELECT c.task.id, COUNT(c)
    FROM Comment c
    WHERE c.task.id IN :taskIds
    GROUP BY c.task.id
    """)
    List<Object[]> countCommentsByTaskIds(
            @Param("taskIds") List<Long> taskIds
    );
}
