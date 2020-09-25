package com.zoo.room.animal.repository;

import com.zoo.room.animal.model.Animal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long>{

   @Query("Select a from Animal a where a.room is null")
   List<Animal> findAllAnimalsInZoo(Sort sort);

   @Query("Select a from Animal a where a.room.id=?1")
   List<Animal> findAllAnimalsByRoomId(Long roomId,Sort sort);
}
