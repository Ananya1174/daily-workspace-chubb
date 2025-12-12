package com.example.service;

import com.example.model.Person;
import com.example.repository.PersonRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {
	private final PersonRepository repo;

	public PersonService(PersonRepository repo) {
		this.repo = repo;
	}

	public List<Person> saveCsv(MultipartFile file) throws IOException {
		List<Person> saved = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
				CSVReader reader = new CSVReader(br)) {
			String[] line;
			boolean first = true;
			while ((line = reader.readNext()) != null) {
				if (first) {
					first = false;
					continue;
				}
				String name = line.length > 0 ? line[0].trim() : "";
				String email = line.length > 1 ? line[1].trim() : "";
				Integer age = null;
				try {
					age = line.length > 2 && !line[2].trim().isEmpty() ? Integer.valueOf(line[2].trim()) : null;
				} catch (NumberFormatException e) {
					age = null;
				}
				Person p = new Person(null, name, email, age);
				saved.add(repo.save(p));
			}
		} catch (CsvValidationException e) {
			throw new IOException("CSV parse error", e);
		}
		return saved;
	}

	public List<Person> getAll() {
		return repo.findAll();
	}
}
