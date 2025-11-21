package com.example.service;

import com.example.model.Tutorial;
import com.example.repository.TutorialRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TutorialService {

	private final TutorialRepository repository;

	public TutorialService(TutorialRepository repository) {
		this.repository = repository;
	}

	public Mono<Tutorial> create(Tutorial t) {
		return repository.save(t);
	}

	public Flux<Tutorial> findAll() {
		return repository.findAll();
	}

	public Mono<Tutorial> findById(Long id) {
		return repository.findById(id);
	}

	public Mono<Tutorial> update(Long id, Tutorial t) {
		return repository.findById(id).flatMap(existing -> {
			existing.setTitle(t.getTitle());
			existing.setDescription(t.getDescription());
			existing.setPublished(t.getPublished());
			return repository.save(existing);
		});
	}

	public Mono<Void> delete(Long id) {
		return repository.deleteById(id);
	}

	public Mono<Void> deleteAll() {
		return repository.deleteAll();
	}

	public Flux<Tutorial> findByPublished(Boolean p) {
		return repository.findByPublished(p);
	}

	public Flux<Tutorial> findByTitle(String title) {
		return repository.findByTitleContaining(title);
	}
}