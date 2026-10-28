package taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "tasks")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @Enumerated(EnumType.STRING)
    private TaskStatus status = TaskStatus.CREATED;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private UserAccount author;

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public UserAccount getAuthor() {
        return author;
    }

    public void setAuthor(UserAccount author) {
        this.author = author;
    }
}

