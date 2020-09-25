package com.zoo.room.animal.service;

import com.zoo.room.animal.model.Room;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface RoomService {
    List<Room> findAll();
    Optional<Room> findById(Long roomId);
    Room save(Room room);
    void delete(Room room);
    void deleteAll();
    HashMap<String,Long> findAllHappyAnimals();
}
