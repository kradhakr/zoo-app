package com.zoo.room.animal.controller;

import com.zoo.room.animal.exception.ResourceNotFoundException;
import com.zoo.room.animal.model.Animal;
import com.zoo.room.animal.model.Room;
import com.zoo.room.animal.service.AnimalService;
import com.zoo.room.animal.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private AnimalService animalService;

    @GetMapping("/room")
    public List<Room> getRoom() {
        return roomService.findAll();
    }

    @GetMapping("/room/{id}")
    public Room getRoomById(
            @PathVariable(value = "id") Long roomId) throws ResourceNotFoundException {
        return this.roomService.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
    }

    @PostMapping("/room")
    public Room createRoom(@Valid @RequestBody Room room) {
        return roomService.save(room);
    }

    @PutMapping("/room/{id}")
    public Room updateRoom(@PathVariable(value = "id") Long roomId,
                                               @Valid @RequestBody Room roomDetails) throws ResourceNotFoundException {
        return this.roomService.findById(roomId)
                .map(room -> {
                    room.setTitle(roomDetails.getTitle());
                    room.setSize(roomDetails.getSize());
                    room.setCreatedDate(roomDetails.getCreatedDate());
                    return this.roomService.save(room);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for id:: " + roomId));
    }

    @PutMapping("/room/{id}/animal/{animalId}")
    public Room addAnimalToRoomByID(@PathVariable(value = "id") Long roomId,
                                  @PathVariable(value = "animalId") Long animalId
                                       ) throws ResourceNotFoundException {
        Room roomRequest = roomService.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
        Animal animalRequest = animalService.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
        animalRequest.setRoom(roomRequest);
        animalService.save(animalRequest);

        List<Animal> animalList = roomRequest.getAnimalList();
        animalList.remove(animalRequest);
        animalList.add(animalRequest);
        return roomService.save(roomRequest);
    }

    @PutMapping("/room/{roomId1}/{roomId2}/animal/{animalId}")
    public ResponseEntity<String>  moveAnimalFromRoomByID(@PathVariable(value = "roomId1") Long roomId1,
                                       @PathVariable(value = "roomId2") Long roomId2,
                                       @PathVariable(value = "animalId") Long animalId
    ) throws ResourceNotFoundException {
        Room room1 = roomService.findById(roomId1)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId1));
        Room room2 = roomService.findById(roomId2)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId2));
        Animal animalRequest = animalService.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
        try {
            room1.getAnimalList().remove(animalService.findById(animalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId)));
            roomService.save(room1);
            room2.getAnimalList().add(animalRequest);
            roomService.save(room2);
            animalRequest.setRoom(room2);
            animalService.save(animalRequest);
            return new ResponseEntity<>("Animal moved to room "+roomId2, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("There was a problem in moving Animal", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/room/{id}/animal/{animalId}")
    public ResponseEntity<String> deleteAnimalFromRoomById(@PathVariable(value = "id") Long roomId,
                                                               @PathVariable(value = "animalId") Long animalId)
            throws ResourceNotFoundException {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
        Animal animal = animalService.findById(animalId)
                .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId));
        try {
            room.getAnimalList().remove(animalService.findById(animalId)
                    .orElseThrow(() -> new ResourceNotFoundException("Animal not found :: " + animalId)));
            roomService.save(room);
            animal.setRoom(null);
            animalService.save(animal);
            return new ResponseEntity<>("Deleted animal from room "+roomId, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Animal was not deleted "+roomId,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping("/room/{id}")
    public String deleteRoom(@PathVariable(value = "id") Long roomId) throws ResourceNotFoundException {
        Room room = roomService.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found for id :: " + roomId));
        roomService.delete(room);
        return "Deleted Room with id :: " + roomId;
    }


    @DeleteMapping("/room")
    public String deleteAllRoom() {
        this.roomService.deleteAll();
        return "All rooms are deleted successfully";
    }


    @GetMapping("/room/{roomId}/animals")
    public List<Animal> getAnimalByRoomId(@PathVariable(value = "roomId") Long roomId) throws ResourceNotFoundException {
        return roomService.findById(roomId).map(room -> {
            List<Animal> animalList = room.getAnimalList();
            return animalList;
        })
                .orElseThrow(() -> new ResourceNotFoundException("Room not found :: " + roomId));
    }

    @GetMapping("/room/happyAnimals")
    public Map<String, Long> getAllHappyAnimals(){
        return roomService.findAllHappyAnimals();
    }
}
