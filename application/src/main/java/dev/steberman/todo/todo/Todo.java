package dev.steberman.todo.todo;

import org.springframework.format.annotation.DateTimeFormat;

import dev.steberman.todo.collaboration.TodoCollaborationRequest;
import dev.steberman.todo.person.Person;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Size(max = 30)
  private String title;

  @Size(max = 100)
  private String description;

  @Enumerated(EnumType.STRING)
  private Priority priority;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;

  @Enumerated(EnumType.STRING)
  private Status status;

  @ManyToOne
  @JoinColumn(name = "owner_id")
  private Person owner;

  // @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  // @JoinColumn(name = "todo_id")
  // private List<Reminder> reminders;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "todo_id")
  private List<Note> notes;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "todo_id")
  private List<TodoCollaborationRequest> collaborationRequests;

  @ManyToMany
  @JoinTable(name = "todo_collaboration",
    joinColumns = @JoinColumn(name = "todo_id"),
    inverseJoinColumns = @JoinColumn(name = "collaborator_id")
  )
  private List<Person> collaborators;

  

  @Override
  public String toString() {
    return "Todo{" +
      "id=" + id +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", priority=" + priority +
      ", dueDate=" + dueDate +
      ", status=" + status +
      ", owner=" + owner +
      '}';
  }
}
