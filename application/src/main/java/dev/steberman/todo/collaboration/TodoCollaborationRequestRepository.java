package dev.steberman.todo.collaboration;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dev.steberman.todo.person.Person;
import dev.steberman.todo.todo.Todo;

@Repository
public interface TodoCollaborationRequestRepository extends CrudRepository<TodoCollaborationRequest, Long> {

  Optional<TodoCollaborationRequest> findByTodoAndCollaborator(Todo todo, Person person);
}

