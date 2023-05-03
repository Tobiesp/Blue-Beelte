package com.tspdevelopment.bluebeetle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
//@EnableAutoConfiguration(exclude = {ErrorMvcAutoConfiguration.class})
public class BlueBeetleApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlueBeetleApplication.class, args);
	}

}
