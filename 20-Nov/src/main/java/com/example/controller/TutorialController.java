package com.example.controller;

import com.example.model.Tutorial;
import com.example.service.TutorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/tutorials")
public class TutorialController {

	private final TutorialService service;

	public TutorialController(TutorialService service) {
		this.service = service;
	}

	/**
	 * POST /api/tutorials - Creates a tutorial - Returns 201 Created - Response
	 * body contains only { id, published } - Location header points to the new
	 * resource
	 */
	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> createTutorial(@RequestBody Tutorial tutorial) {
		return service.create(tutorial).map(saved -> {
			Map<String, Object> body = Map.of("id", saved.getId(), "published", saved.getPublished());
			URI location = URI.create("/api/tutorials/" + saved.getId());
			return ResponseEntity.created(location).body(body);
		});
	}

	/**
	 * GET /api/tutorials - Returns all tutorials (200 OK)
	 */
	@GetMapping
	public Flux<Tutorial> getAllTutorials() {
		return service.findAll();
	}

	/**
	 * GET /api/tutorials/{id} - Returns 200 OK with Tutorial if found - Returns 404
	 * Not Found if missing
	 */
	@GetMapping("/{id}")
	public Mono<ResponseEntity<Tutorial>> getTutorialById(@PathVariable Long id) {
		return service.findById(id).map(t -> ResponseEntity.ok(t)).defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/**
	 * PUT /api/tutorials/{id} - Updates a tutorial - Returns 200 OK with updated
	 * Tutorial if found - Returns 404 Not Found if missing
	 */
	@PutMapping("/{id}")
	public Mono<ResponseEntity<Tutorial>> updateTutorial(@PathVariable Long id, @RequestBody Tutorial tutorial) {
		return service.update(id, tutorial).map(updated -> ResponseEntity.ok(updated))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	/**
	 * DELETE /api/tutorials/{id} - Deletes a tutorial by id - Returns 200 OK with
	 * message if deleted - Returns 404 Not Found if missing
	 */
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Map<String, String>>> deleteTutorial(@PathVariable Long id) {
		return service.findById(id)
				.flatMap(existing -> service.delete(id)
						.then(Mono.just(ResponseEntity.ok(Map.of("message", "Deleted successfully")))))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
