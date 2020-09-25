package com.zoo.room.animal.repository;

import com.zoo.room.animal.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long>{
    @Query("Select distinct r.title from Room r join Favorite f on r.id=f.roomId where f.animalId=?1")
    List<String> findAllFavoriteRoomByAnimalId(@Param("animalId") long animalId);

}
