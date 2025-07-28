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
import jakarta.validation.constraints.Size;


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
	
	@NotBlank
	private String genere;
	
	private String album;
	
	@NotNull
	private Integer durataMinuti;
	
	@Column(length=5000)
	@Size(max=2000, message="la lunghezza del testo non può superare i 2000 caratteri")
	private String testo;
	
	@ManyToMany
	private Set<Autore> autori;
	
	@OneToMany(mappedBy="brano", cascade={CascadeType.REMOVE})
	private List<Commento> commenti;
	
	private String immagine;
	
	
	
	public Long getId() {
		return id;
	}
	public String getTitolo() {
		return titolo;
	}
	public Integer getAnno() {
		return anno;
	}
	public String getGenere() {
		return genere;
	}
	public String getAlbum() {
		return album;
	}
	public Integer getDurataMinuti() {
		return durataMinuti;
	}
	public String getTesto() {
		return testo;
	}
	public Set<Autore> getAutori() {
		return autori;
	}
	public List<Commento> getCommenti() {
		return commenti;
	}
	public String getImmagine() {
		return immagine;
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
	public void setGenere(String genere) {
		this.genere = genere;
	}
	public void setAlbum(String album) {
		this.album = album;
	}
	public void setDurataMinuti(Integer durataMinuti) {
		this.durataMinuti = durataMinuti;
	}
	public void setTesto(String testo) {
		this.testo = testo;
	}
	public void setAutori(Set<Autore> autori) {
		this.autori = autori;
	}
	public void setCommenti(List<Commento> commenti) {
		this.commenti = commenti;
	}
	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
	
	

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
	    if (o == null || getClass() != o.getClass()) return false;

	    Brano brano = (Brano) o;

	    if (!titolo.equalsIgnoreCase(brano.titolo)) return false;
	    if (!anno.equals(brano.anno)) return false;
	    if (!genere.equalsIgnoreCase(brano.genere)) return false;
	    if (album != null ? !album.equalsIgnoreCase(brano.album) : brano.album != null) return false;
	    if (!durataMinuti.equals(brano.durataMinuti)) return false;
	    if (testo != null ? !testo.equals(brano.testo) : brano.testo != null) return false;
	    if (autori != null ? !autori.equals(brano.autori) : brano.autori != null) return false;

	    return true;
	}
	
	
	@Override
	public int hashCode() {
		int result = titolo.toLowerCase().hashCode();
	    result = 31 * result + anno.hashCode();
	    result = 31 * result + genere.toLowerCase().hashCode();
	    result = 31 * result + (album != null ? album.toLowerCase().hashCode() : 0);
	    result = 31 * result + durataMinuti.hashCode();
	    result = 31 * result + (testo != null ? testo.hashCode() : 0);
	    result = 31 * result + (autori != null ? autori.hashCode() : 0);
	    return result;
	}
}
