package dev.steberman.todo.todo;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Entity
@Data
public class Reminder {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotEmpty
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate dueDate;

}
