package es.marcosaguirre.videogame.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import es.marcosaguirre.videogame.common.GameTypes;
import lombok.Data;

@Data
@Entity
@Table(name = "inventory")
public class VideoGame {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", nullable = false, length = 70)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type_of_game", nullable = false, length = 70)
	private GameTypes typeOfGame;
	
	@Column(name = "available", columnDefinition =  "boolean default true")
	private boolean available;
	
	@Column(name = "start_rental_date", nullable = true, columnDefinition =  "date default null")
	private LocalDate startRentalDate;
	
	@Column(name = "end_rental_date", nullable = true, columnDefinition =  "date default null")
	private LocalDate endRentalDate;
	
	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true, foreignKey = @ForeignKey(name = "FK_videogame_user"))
	@JsonBackReference
	private Customer customer;

	
}
