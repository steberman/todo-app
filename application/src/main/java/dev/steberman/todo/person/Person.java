package dev.steberman.todo.person;

import dev.steberman.todo.collaboration.TodoCollaborationRequest;
import dev.steberman.todo.todo.Todo;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Person {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @Column(unique = true)
  private String name;

  @NotEmpty
  @Column(unique = true)
  private String email;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "owner")
  private List<Todo> ownedTodos = new ArrayList<>();

  @ManyToMany(mappedBy = "collaborators")
  private List<Todo> collaborativeTodos = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "collaborator")
  private List<TodoCollaborationRequest> collaborationRequests = new ArrayList<>();

  
}
