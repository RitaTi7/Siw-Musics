package it.uniroma3.siw.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@NotBlank
	private String email;
	
	@OneToMany(mappedBy="user", cascade= CascadeType.REMOVE)
	private List<Commento> commenti;
	
	public Long getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	public String getSurname() {
		return surname;
	}
	public String getEmail() {
		return email;
	}
	public List<Commento> getCommenti() {
		return commenti;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setName(String nome) {
		this.name = nome;
	}
	public void setSurname(String cognome) {
		this.surname = cognome;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		
		
		if(id==null) {
			if(other.id!=null)
				return false;
		} else if(!id.equals(other.id)) {
			return false;
		}
		
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (surname == null) {
			if (other.surname != null)
				return false;
		} else if (!surname.equals(other.surname))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((surname == null) ? 0 : surname.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
	    return "User{name='" + name + "', surname='" + surname + "', email='" + email + "'}";
	}
	
}
