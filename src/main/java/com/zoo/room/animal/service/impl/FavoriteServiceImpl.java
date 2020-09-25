package com.zoo.room.animal.service.impl;

import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.AnimalType;
import com.zoo.room.animal.model.Favorite;
import com.zoo.room.animal.model.Room;
import com.zoo.room.animal.repository.FavoriteRepository;
import com.zoo.room.animal.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.zoo.room.animal.model.AnimalType.GREATER_THAN_EQUAL;
import static com.zoo.room.animal.model.AnimalType.LESS_THAN_EQUAL;


@Service
public class FavoriteServiceImpl implements FavoriteService {
    @Autowired
    private FavoriteRepository favoriteRepository;

    @Override
    public String save(Favorite favorite, Animal animal, Room room) {
        //ToDo  need to check
        String message = "";
        String type = animal.getType();
        Long preference = animal.getPreference();
        Long roomSize = room.getSize();
        boolean addFavorite = false;

        if(type!=null && (type.equals(GREATER_THAN_EQUAL) || type.equals(LESS_THAN_EQUAL) )){
          if(type.equals(GREATER_THAN_EQUAL)){
                if(roomSize>=preference){
                    addFavorite = true;
                }
               }else if(type.equals(LESS_THAN_EQUAL)){
                if(roomSize<=preference){
                    addFavorite = true;
                }else
               {
                addFavorite = false;
                }
           }

            if(addFavorite) {
                favoriteRepository.delete(favorite);
                favoriteRepository.save(favorite);
                message = "Assigned room to animal as favorite";
            }else{
                message = "Room cannot be added as favorite because the animal is not happy";
            }
        }
        return message;
    }

    @Override
    public List<Favorite> findAll() { return favoriteRepository.findAll(); }

    @Override
    public void delete(Favorite favorite) {
        favoriteRepository.delete(favorite);
    }

    @Override
    public Optional<Favorite> findById(Long favoriteId) {
        return favoriteRepository.findById(favoriteId);
    }

    @Override
    public List<String> findAllFavoriteRoomByAnimalId(Long roomId) {
        return favoriteRepository.findAllFavoriteRoomByAnimalId(roomId);
    }

}
