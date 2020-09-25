package com.zoo.room.animal.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Entity
@Table(name = "room")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id",
		scope = Room.class)
public class Room extends AuditModel{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "size")
	private long size;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "ra_id", referencedColumnName = "id")
	private List<Animal> animalList;

	public Room() {
		this.animalList = new ArrayList<>();
	}

	public Room(String title, Long size) {
		this.title = title;
		this.size = size;
		this.animalList = new ArrayList<>();
	}
}
