package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.repository.AutoreRepository;

@Service
public class AutoreService {
	
	@Autowired AutoreRepository autoreRepository;
	
	public Autore getAutoreById(Long id) {
		Optional<Autore> a= this.autoreRepository.findById(id);
		if(a.isEmpty())
			return null;
		else
			return a.get();
	}
	
	public Iterable<Autore> getAllAutori(){
		return this.autoreRepository.findAll();
	}
	
	public void save(Autore autore) {
		this.autoreRepository.save(autore);
	}
	
	public void deleteAutoreById(Long id) {
		this.autoreRepository.deleteById(id);
	}
	
	public Iterable<Autore> findAutoriNonInBrano(Long idBrano){
		return this.autoreRepository.findAutoriNonInBrano(idBrano);
		
	}
}
