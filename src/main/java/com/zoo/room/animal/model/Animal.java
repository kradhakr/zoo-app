package com.zoo.room.animal.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "animal")
@JsonIdentityInfo(
		generator = ObjectIdGenerators.PropertyGenerator.class,
		property = "id",
		scope = Animal.class)
public class Animal extends AuditModel{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@JsonDeserialize(using = LocalDateDeserializer.class)
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonFormat(pattern="yyyy-MM-dd")
	@Column(name = "located_date", nullable = false)
	private LocalDate locatedDate;

	@Column(name = "type", nullable = false)
	private String type;

	@Column(name = "preference")
	private long preference;

	@JsonIgnore
	@ManyToOne(cascade= CascadeType.ALL)
	private Room room;

	public Animal(String title, LocalDate  locatedDate,String type,long preference) {
		this.title = title;
		this.locatedDate = locatedDate;
		this.type = type;
		this.preference = preference;
	}

	public Animal(String title, LocalDate locatedDate,String type,long preference,Room room) {
		this.title = title;
		this.locatedDate = locatedDate;
		this.type = type;
		this.preference = preference;
		this.room = room;
	}
}
