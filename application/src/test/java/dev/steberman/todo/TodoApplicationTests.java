package dev.steberman.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Disabled;

@Disabled("Disabled until introducing test containers")
@ActiveProfiles("dev")
@SpringBootTest
class TodoApplicationTests {


	@Autowired
	private ApplicationContext applicationContext;
  
	@Test
	void contextLoads() {
		// succeeds if spring successfully starts up
		assertNotNull(applicationContext);
	}

}
