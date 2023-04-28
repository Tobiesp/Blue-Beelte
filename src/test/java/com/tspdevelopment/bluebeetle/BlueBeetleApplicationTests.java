package com.tspdevelopment.bluebeetle;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest(classes = BlueBeetleApplication.class)
@ActiveProfiles("test")
class BlueBeetleApplicationTests {

	@Test
	void contextLoads() {
	}

}
