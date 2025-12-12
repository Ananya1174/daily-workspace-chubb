package com.example.controller;

import com.example.model.Person;
import com.example.service.PersonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UploadController {
	private final PersonService service;

	public UploadController(PersonService service) {
		this.service = service;
	}

	@PostMapping("/upload")
	public ResponseEntity<List<Person>> upload(@RequestParam("file") MultipartFile file) throws Exception {
		if (file == null || file.isEmpty())
			return ResponseEntity.badRequest().build();
		List<Person> saved = service.saveCsv(file);
		return ResponseEntity.ok(saved);
	}

	@GetMapping("/persons")
	public ResponseEntity<List<Person>> all() {
		return ResponseEntity.ok(service.getAll());
	}
}
