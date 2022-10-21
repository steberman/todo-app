package dev.steberman.todo.dashboard;

import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
public class CollaboratorDto {

  @Getter
  private final Long id;

  @Getter
  private final String name;

}
