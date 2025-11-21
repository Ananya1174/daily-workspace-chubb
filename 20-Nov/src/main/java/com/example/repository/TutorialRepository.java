package com.example.repository;

import com.example.model.Tutorial;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface TutorialRepository extends R2dbcRepository<Tutorial, Long> {
	Flux<Tutorial> findByPublished(Boolean published);

	Flux<Tutorial> findByTitleContaining(String title);
}