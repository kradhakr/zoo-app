package com.zoo.room.animal.controller;

import com.zoo.room.animal.exception.ResourceNotFoundException;
import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Favorite;
import com.zoo.room.animal.model.Room;
import com.zoo.room.animal.service.AnimalService;
import com.zoo.room.animal.service.FavoriteService;
import com.zoo.room.animal.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class FavoriteController {
    @Autowired
    private FavoriteService favoriteService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private AnimalService animalService;

    @GetMapping("/favorite")
    public List<Favorite> getAllFavorite() {
        return favoriteService.findAll();
    }

    @PostMapping("/favorite")
    public String createFavorite(@Valid @RequestBody Favorite favorite) throws ResourceNotFoundException {
        long roomId = favorite.getRoomId();
        long animalId = favorite.getAnimalId();
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
        Animal animal = animalService.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
        return favoriteService.save(favorite,animal,room);
    }

    @GetMapping("/favorite/{id}")
    public Favorite getFavoriteById(@PathVariable(value = "id") Long favoriteId) throws ResourceNotFoundException {
        return this.favoriteService.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found :: " + favoriteId));
    }


    @DeleteMapping("/favorite/{id}")
    public String deleteFavorite(@PathVariable(value = "id") Long favoriteId) throws ResourceNotFoundException {
        Favorite favorite = favoriteService.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found for this id :: " + favoriteId));
        favoriteService.delete(favorite);
        return "Deleted Favorite with id :: "+ favoriteId;
    }

    @GetMapping("/favorite/animal/{animalId}")
    public List<String> getFavoriteRoomByAnimalId(@PathVariable(value = "animalId") Long animalId) throws ResourceNotFoundException {
          if(!animalService.existsById(animalId)) {
            throw new ResourceNotFoundException("Animal Id " + animalId + " not found");
         }
         return favoriteService.findAllFavoriteRoomByAnimalId(animalId);
        }
    }
