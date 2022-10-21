package dev.steberman.todo.dashboard;

import java.time.LocalDate;

import dev.steberman.todo.todo.Todo;
import lombok.Getter;

public class TodoDto {

  @Getter
  private Long id;

  @Getter
  private String title;

  @Getter
  private int amountOfCollaborators;

  @Getter
  private int amountOfCollaborationRequests;

  @Getter
  private LocalDate dueDate;

  //@Getter
  private boolean isCollaboration;

  public boolean isCollaboration() {
    return isCollaboration;
  }

  public TodoDto(Todo todo, boolean isCollaboration) {
    this.id = todo.getId();
    this.title = todo.getTitle();
    this.amountOfCollaborationRequests = todo.getCollaborationRequests().size();
    this.amountOfCollaborators = todo.getCollaborators().size();
    this.dueDate = todo.getDueDate();
    this.isCollaboration = isCollaboration;
  }

  
}
