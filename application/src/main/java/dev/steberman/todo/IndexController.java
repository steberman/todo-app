package dev.steberman.todo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

  

  @GetMapping
  @RequestMapping("/")
  public String getIndex() {
    return "This is the revised steberman's todo-app.";
    
  }
}
