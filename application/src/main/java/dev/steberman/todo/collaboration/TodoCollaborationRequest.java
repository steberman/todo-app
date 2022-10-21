package dev.steberman.todo.collaboration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import dev.steberman.todo.person.Person;
import dev.steberman.todo.todo.Todo;
import lombok.Data;

@Entity
@Data
public class TodoCollaborationRequest {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String token;

  @ManyToOne
  @JoinColumn(name = "collaborator_id")
  private Person collaborator;

  @ManyToOne
  private Todo todo;

  @Override
  public String toString() {
    return "TodoCollaborationRequest{" +
      "todoId=" + todo.getId() +
      ", todoTitle='" + todo.getTitle() + '\'' +
      ", todoDescription='" + todo.getDescription() + '\'' +
      ", todoPriority=" + todo.getPriority() +
      ", collaboratorId=" + collaborator.getId() +
      ", collaboratorName='" + collaborator.getName() + '\'' +
      ", collaboratorEmail='" + collaborator.getEmail() + '\'' +
      '}';
  }
}
