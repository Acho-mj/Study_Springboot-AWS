package com.acho.studyAws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class StudyAwsApplication {
	public static void main(String[] args) {
		SpringApplication.run(StudyAwsApplication.class, args);
	}
}
