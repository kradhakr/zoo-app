package com.zoo.room.animal.controller;

import java.util.*;
import javax.validation.Valid;
import com.zoo.room.animal.exception.ResourceNotFoundException;
import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.service.AnimalService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AnimalController {
	private static final Log LOG = LogFactory.getLog(AnimalController.class);

	@Autowired
	private AnimalService animalService;

	@GetMapping("/animals")
	public List<Animal> getAllAnimals() {
		return animalService.findAll();
	}

	@GetMapping("/animals/{id}")
	public Animal getAnimalById(@PathVariable(value = "id") Long animalId)
			throws ResourceNotFoundException {
		return this.animalService.findById(animalId)
				.orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
	}

	@PostMapping("/animals")
	public Animal createAnimal(@Valid @RequestBody Animal animal) {
		return animalService.save(animal);
	}

	@PutMapping("/animals/{id}")
	public Animal updateAnimal(@PathVariable(value = "id") Long animalId,
											   @Valid @RequestBody Animal animalDetails) throws ResourceNotFoundException {
		return this.animalService.findById(animalId)
				.map(animal -> {
					animal.setTitle(animalDetails.getTitle());
					animal.setLocatedDate(animalDetails.getLocatedDate());
					animal.setType(animalDetails.getType());
					animal.setPreference(animalDetails.getPreference());
					return this.animalService.save(animal);
				})
				.orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
	}

	@DeleteMapping("/animals/{id}")
	public String deleteAnimal(@PathVariable(value = "id") Long animalId) throws ResourceNotFoundException {
		Animal animal = animalService.findById(animalId)
				.orElseThrow(() -> new ResourceNotFoundException("Animal not found for this id :: " + animalId));
		animalService.delete(animal);
		return "Deleted animal with id  :: "+animalId;
	}

	@DeleteMapping("/animals")
	public  String  deleteAllAnimals() {
		this.animalService.deleteAll();
		return "All animals are deleted successfully";
	}

	@GetMapping("/animals/zoo")
	public List<Animal> getAllAnimalsInZoo(@RequestParam("column") String column,
											   @RequestParam("order") String order) {
		List<Animal> animalList = new ArrayList<>();
		if (order != null) {
			if (order.equalsIgnoreCase("desc")) {
				animalList = animalService.findAllAnimalsInZoo(Sort.by(column).descending());
			} else {
				animalList = animalService.findAllAnimalsInZoo(Sort.by(column));
			}
		}
		return animalList;
	}


	@GetMapping("/animals/room/{roomId}")
	public List<Animal> getAllAnimalsByRoomId(@PathVariable(value = "roomId") Long roomId,
											  @RequestParam("column") String column,
										      @RequestParam("order") String order) {
		List<Animal> animalList = new ArrayList<>();
		if (order != null) {
			if (order.equalsIgnoreCase("desc")) {
				animalList = animalService.findAllAnimalsByRoomId(roomId,Sort.by(column).descending());
			} else {
				animalList = animalService.findAllAnimalsByRoomId(roomId,Sort.by(column));
			}
		}
		return animalList;
	}

}