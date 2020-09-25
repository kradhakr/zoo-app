package com.zoo.room.animal.service.impl;

import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Room;
import com.zoo.room.animal.repository.AnimalRepository;
import com.zoo.room.animal.repository.RoomRepository;
import com.zoo.room.animal.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.zoo.room.animal.model.AnimalType.GREATER_THAN_EQUAL;
import static com.zoo.room.animal.model.AnimalType.LESS_THAN_EQUAL;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;

    public List<Room> findAll() {
        return roomRepository.findAll();
    }

    @Override
    public Optional<Room> findById(Long roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public void delete(Room room) {
        roomRepository.delete(room);
    }

    @Override
    public void deleteAll() {
        roomRepository.deleteAll();
    }

    /**
     * Retrieve
     * @return List of HappyAnimals
     * Animal is happy when located in Room and preferences and size of the room match
     */

    @Override
    public HashMap<String, Long> findAllHappyAnimals() {
        List<Room> rooms = roomRepository.findAll();
        Map<String, Long> result = new HashMap<>();
        long count;
        for(Room room : rooms){
            long roomSize = room.getSize();
            String roomTitle = room.getTitle();
            count = 0;
            List<Animal> animals = room.getAnimalList();
            for(Animal animal : animals){
                long preference = animal.getPreference();
                String type = animal.getType();
                if(type!=null && type.equals(LESS_THAN_EQUAL)){
                    if(roomSize<=preference){
                        count++;
                    }
                }else if(type!=null && type.equals(GREATER_THAN_EQUAL)){
                    if(roomSize>=preference){
                        count++;
                    }
                }
            }
            result.put(roomTitle, count);
        }

        LinkedHashMap<String, Long> happyAnimalMap =  result.entrySet()
                .stream()
                .sorted((Map.Entry.<String, Long>comparingByValue().reversed()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e2, e1) -> e2, LinkedHashMap::new));

        return happyAnimalMap;
    }

}
