package com.tspdevelopment.KidsScore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.tspdevelopment.kidsscore.KidsScoreApplication;

@SpringBootTest(classes = KidsScoreApplication.class)
@ActiveProfiles("test")
class KidsScoreApplicationTests {

	@Test
	void contextLoads() {
	}

}
