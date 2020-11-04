package com.thehecklers.aircraftservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Optional;

@SpringBootApplication
public class AircraftServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AircraftServiceApplication.class, args);
	}

}

@Component
@RequiredArgsConstructor
class DataLoader {
	@NonNull
	private final AircraftRepository repo;

	private Map<String, String> ac = Map.of("A380", "Airbus 380",
			"B737", "Boeing 737",
			"LJ75", "Learjet 75");

	@PostConstruct
	void loadData() {
		repo.deleteAll();

		ac.forEach((k, v) -> repo.save(new Aircraft(k, v)));

		repo.findAll().forEach(System.out::println);
	}
}

@RestController
@RequestMapping("/aircraft")
@AllArgsConstructor
class AircraftController {
	private final AircraftRepository repository;

	@GetMapping
	Iterable<Aircraft> getAllAircraft() {
		return repository.findAll();
	}

	@GetMapping("/{reg}")
	Optional<Aircraft> getAircraftByReg(@PathVariable String reg) {
		return repository.findById(reg);
	}
}

interface AircraftRepository extends CrudRepository<Aircraft, String> {

}

@Data
@Document
class Aircraft {
	@Id
	private String reg;
	@NonNull
	private String type, description;
}