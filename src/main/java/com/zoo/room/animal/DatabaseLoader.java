package com.zoo.room.animal;

import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Favorite;
import com.zoo.room.animal.model.Room;
import com.zoo.room.animal.repository.AnimalRepository;
import com.zoo.room.animal.repository.FavoriteRepository;
import com.zoo.room.animal.repository.RoomRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Component
public class DatabaseLoader implements CommandLineRunner {
    private static final Log LOG = LogFactory.getLog(DatabaseLoader.class);

    private final RoomRepository roomRepository;
    private final AnimalRepository animalRepository;
    private final FavoriteRepository favoriteRepository;
    
    @Autowired
    public DatabaseLoader(RoomRepository roomRepository, AnimalRepository animalRepository, FavoriteRepository favoriteRepository) {
        this.roomRepository = roomRepository;
        this.animalRepository = animalRepository;
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Room green = new Room("Green",100L);
        Room blue = new Room("Blue",70L);
        Room yellow = new Room("Yellow",80L);

        Animal cat = new Animal("Cat", LocalDate.of(2020,01,20), "<=", 10L,green);
        Animal dog = new Animal("Dog", LocalDate.of(2017,10,9), ">=", 20L,blue);
        Animal deer = new Animal("Deer",LocalDate.of(2015,07,15), ">=", 40L,yellow);
        Animal cow = new Animal("Cow", LocalDate.of(2019,12,3), ">=", 90L,green);
        Animal horse = new Animal("Horse",LocalDate.of(2010,03,18), ">=", 70L,blue);
        Animal rabbit = new Animal("rabbit",LocalDate.of(2010,03,18), "<=", 10L);
        Animal sheep = new Animal("Sheep",LocalDate.of(2011,02,11), ">=", 70L);
        Animal bull = new Animal("Bull",LocalDate.of(2010,03,18), ">=", 80L);

        this.animalRepository.save(cat);
        this.animalRepository.save(dog);
        this.animalRepository.save(deer);
        this.animalRepository.save(cow);
        this.animalRepository.save(horse);
        this.animalRepository.save(rabbit);
        this.animalRepository.save(sheep);
        this.animalRepository.save(bull);

        List<Animal> greenRoomAnimals = new ArrayList<>();
        greenRoomAnimals.add(cat);
        greenRoomAnimals.add(cow);
        green.setAnimalList(greenRoomAnimals);
        this.roomRepository.save(green);

        List<Animal> blueRoomAnimals = new ArrayList<>();
        blueRoomAnimals.add(dog);
        blueRoomAnimals.add(horse);
        blue.setAnimalList(blueRoomAnimals);
        this.roomRepository.save(blue);

        List<Animal> yellowRoomAnimals = new ArrayList<>();
        yellowRoomAnimals.add(deer);
        yellow.setAnimalList(yellowRoomAnimals);
        this.roomRepository.save(yellow);

        Favorite favoriteGreenCat = new Favorite(1L,4L);
        this.favoriteRepository.save(favoriteGreenCat);

        Favorite favoriteGreenDog = new Favorite(2L,5L);
        this.favoriteRepository.save(favoriteGreenDog);

        LOG.info("\nZoo Data is created successfully...\n");
    }
    
}
