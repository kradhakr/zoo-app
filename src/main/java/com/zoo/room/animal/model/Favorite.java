package com.zoo.room.animal.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "favorite")
public class Favorite extends AuditModel{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "room_id")
	private long roomId;

	@Column(name = "animal_id")
	private long animalId;

	public Favorite(long roomId, Long animalId) {
		this.roomId = roomId;
		this.animalId = animalId;
	}
}
