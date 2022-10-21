package dev.steberman.todo.todo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.steberman.todo.person.Person;


public interface TodoRepository extends JpaRepository<Todo, Long> {

  List<Todo> findAllByOwner(Person person);

  List<Todo> findAllByOwnerEmail(String email);

  List<Todo> findAllByOwnerEmailOrderByIdAsc(String email);

  List<Todo> findAllByCollaboratorsEmailOrderByIdAsc(String email);
}
