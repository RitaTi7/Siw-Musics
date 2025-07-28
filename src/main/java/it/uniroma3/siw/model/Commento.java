package it.uniroma3.siw.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class Commento {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String titolo;
	
	@NotNull
	@Min(1)
	@Max(5)
	private Integer voto;
	
	@Column(length=2000)
	@Size(max=2000, message="Il commento non può superare i 2000 caratteri")
	private String testo;			//cambia i vincoli di colonna ( varying 255 sono pochi)
	
//	@Transient
//	private String usernameUtente;
	
	@ManyToOne
	private Brano brano;
	
	@ManyToOne
	private User user;
	
	
	public Long getId() {
		return id;
	}
	public String getTitolo() {
		return titolo;
	}
	public Integer getVoto() {
		return voto;
	}
	public String getTesto() {
		return testo;
	}
	public Brano getBrano() {
		return brano;
	}
	public User getUser() {
		return user;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public void setVoto(Integer voto) {
		this.voto = voto;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public void setBrano(Brano brano) {
		this.brano = brano;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || obj.getClass()!=this.getClass())
			return false;
		
		Commento r= (Commento) obj;
		
		return r.getTitolo().equals(this.getTitolo()) && r.getVoto().equals(this.getVoto()) && r.getTesto().equals(this.getTesto());
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode()+this.getTitolo().hashCode()+this.getVoto()+this.getTesto().hashCode();
	}
}
