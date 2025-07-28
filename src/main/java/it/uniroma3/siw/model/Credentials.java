package it.uniroma3.siw.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Credentials {
	
//	public static final String GUESS_ROLE= "GUEST";
	public static final String USER_ROLE= "USER";
	public static final String ADMIN_ROLE= "ADMIN";
	

	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	
	private String role;
	
	@OneToOne(cascade= CascadeType.ALL)
	private User user;
	
	public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getRole() {
		return role;
	}
	public User getUser() {
		return user;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public void setRole(String ruolo) {
		this.role = ruolo;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Credentials other = (Credentials) obj;
		
		if(id==null) {
			if(other.id!=null)
				return false;
		} else if(!id.equals(other.id)) {
			return false;
		}
		
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		
		if(user==null) {
			if(other.user !=null)
				return false;
		}else if(!user.equals(other.user))
			return false;
		
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		
		return true;
 	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}
	
	@Override
	public String toString() {
	    return "Credentials{username='" + username + "', password='" + password + "', role='" + role + "'}";
	}
}
