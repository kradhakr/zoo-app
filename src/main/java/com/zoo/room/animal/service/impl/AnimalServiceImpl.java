    package com.zoo.room.animal.service.impl;

    import com.zoo.room.animal.model.Animal;
    import com.zoo.room.animal.model.Favorite;
    import com.zoo.room.animal.repository.AnimalRepository;
    import com.zoo.room.animal.service.AnimalService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Sort;
    import org.springframework.stereotype.Service;

    import java.util.List;
    import java.util.Optional;

    @Service
    public class AnimalServiceImpl implements AnimalService {
        @Autowired
        private AnimalRepository animalRepository;

        @Override
        public List<Animal> findAll() {
            return animalRepository.findAll();
        }

        @Override
        public Optional<Animal> findById(Long animalId) {
            return animalRepository.findById(animalId);
        }

        @Override
        public Animal save(Animal animal) {
            return animalRepository.save(animal);
        }


        @Override
        public void deleteAll() {
            animalRepository.deleteAll();
        }

        @Override
        public Boolean existsById(Long animalId) {
            return animalRepository.existsById(animalId);
        }

        @Override
        public void delete(Animal animal) {
            animalRepository.delete(animal);
        }

        @Override
        public List<Animal> findAllAnimalsInZoo(Sort sort) {
            return animalRepository.findAllAnimalsInZoo(sort);
        }

        @Override
        public List<Animal> findAllAnimalsByRoomId(Long roomId,Sort sort) {
            return animalRepository.findAllAnimalsByRoomId(roomId,sort);
        }

    }
