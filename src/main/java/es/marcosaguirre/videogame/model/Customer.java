package es.marcosaguirre.videogame.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "name", length = 50, nullable = false)
	private String name;
	
	@Column(name = "last_name", length = 60, nullable = false)
	private String lastName;
	
	@Column(name = "mail", length = 60, nullable = false, unique = true)
	private String mail;
	
	@Column(name = "loyalty_points", columnDefinition = "Integer default 0")
	private Integer loyaltyPoints;
	
	@OneToMany(mappedBy = "customer", cascade = { CascadeType.ALL },fetch = FetchType.EAGER)
	@JsonManagedReference
	private List<VideoGame> rentedGames;

	public Customer(String name, String lastName, String mail) {
		this.name = name;
		this.lastName = lastName;
		this.mail = mail;
	}
	
	
}
