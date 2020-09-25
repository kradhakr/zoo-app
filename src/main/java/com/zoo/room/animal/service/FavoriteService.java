package com.zoo.room.animal.service;

import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Favorite;
import com.zoo.room.animal.model.Room;
import java.util.List;
import java.util.Optional;

public interface FavoriteService {
    String save(Favorite favorite,Animal animal,Room room);
    List<Favorite> findAll();
    void delete(Favorite favorite);
    Optional<Favorite> findById(Long favoriteId);
    List<String> findAllFavoriteRoomByAnimalId(Long roomId);
}
