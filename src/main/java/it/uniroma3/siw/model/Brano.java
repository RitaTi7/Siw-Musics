package it.uniroma3.siw.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
public class Brano {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String titolo;
	
	@NotNull
	@Max(2025)
	private Integer anno;
	
//	@NotBlank
//	@Column(length= 500)
//	private String trama;	//TODO
//	
//	@NotBlank
//	private String genere;	//TODO
	
	@ManyToMany
	private Set<Autore> autori;
	
	@OneToMany(mappedBy="brano", cascade={CascadeType.REMOVE})
	private List<Commento> commenti;
	
	@ElementCollection
	List<String> immagini = new ArrayList<>();
	
	//collana			//TODO
	//genere
	//casa editrice??
	
	
	public Long getId() {
		return id;
	}
	public String getTitolo() {
		return titolo;
	}
	public Integer getAnno() {
		return anno;
	}
//	public String getTrama() {
//		return trama;
//	}
	public Set<Autore> getAutori() {
		return autori;
	}
	public List<Commento> getCommenti() {
		return commenti;
	}
	public List<String> getImmagini() {
		return immagini;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}
	public void setAnno(Integer anno) {
		this.anno = anno;
	}
//	public void setTrama(String trama) {
//		this.trama = trama;
//	}
	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}
	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}
	public void setImmagini(List<String> immagini) {
		this.immagini = immagini;
	}
	

	@Override
	public boolean equals(Object obj) {
		if(obj==null || obj.getClass()!=this.getClass())
			return false;
		
		Brano l= (Brano) obj;
		
		return l.getTitolo().equals(this.getTitolo()) && l.getAnno().equals(this.getAnno());
	}
	
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode()+ this.getTitolo().hashCode()+ this.getAnno();
	}
}
