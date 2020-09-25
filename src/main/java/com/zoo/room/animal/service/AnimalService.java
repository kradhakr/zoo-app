package com.zoo.room.animal.service;

import com.zoo.room.animal.model.Animal;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

public interface AnimalService {
    List<Animal> findAll();
    Optional<Animal> findById(Long animalId);
    Animal save(Animal animal);
    void delete(Animal animal);
    void deleteAll();
    Boolean existsById(Long animalId);
    List<Animal> findAllAnimalsInZoo(Sort sort);
    List<Animal> findAllAnimalsByRoomId(Long roomId,Sort sort);

}
