package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;

@Entity
public class Autore {
	
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	
	@NotBlank
	private String nomeArte;
	
	@NotNull
	@DateTimeFormat(pattern= "yyyy-MM-dd")
	@Past
	private LocalDate dataNascita;		//TODO: la nascita non può superare la morte->annotazione personalizzata, controllo nel Controller...
	
	@DateTimeFormat(pattern= "yyyy-MM-dd")
	@PastOrPresent(message="la data non può essere futura")
	private LocalDate dataMorte;
	@NotBlank
	private String nazionalita;
	
	private String immagine;
	
	
	@ManyToMany(mappedBy="autori")
	private List<Brano> brani;
	
	//fotografia
	
	
	public Long getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	public String getCognome() {
		return cognome;
	}
	public String getNomeArte() {
		return nomeArte;
	}
	public LocalDate getDataNascita() {
		return dataNascita;
	}
	public LocalDate getDataMorte() {
		return dataMorte;
	}
	public String getNazionalita() {
		return nazionalita;
	}
	public List<Brano> getBrani() {
		return brani;
	}
	public String getImmagine() {
		return immagine;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public void setNomeArte(String nomeArte) {
		this.nomeArte = nomeArte;
	}
	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}
	public void setDataMorte(LocalDate dataMorte) {
		this.dataMorte = dataMorte;
	}
	public void setNazionalita(String nazionalità) {
		this.nazionalita = nazionalità;
	}
	public void setBrani(List<Brano> brano) {
		this.brani = brano;
	}
	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj==null || obj.getClass()!=this.getClass())
			return false;
		
		Autore a= (Autore) obj;
		
		return a.getNome().equals(this.getNome()) && a.getCognome().equals(this.getCognome()) && a.getDataNascita().equals(this.getDataNascita()) && a.getNazionalita().equals(this.getNazionalita()) && a.getNomeArte().equals(this.getNomeArte());
	}
	
	@Override
	public int hashCode() {
		return this.getClass().hashCode()+this.getNome().hashCode()+this.getCognome().hashCode()+this.getDataNascita().hashCode()+this.getNazionalita().hashCode() + this.getNomeArte().hashCode();
	}

}
