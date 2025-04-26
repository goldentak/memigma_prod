package kz.memigma.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MemigmaApplication {
	public static void main(String[] args) {
		SpringApplication.run(MemigmaApplication.class, args);
	}
}
