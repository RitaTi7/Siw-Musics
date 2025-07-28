package it.uniroma3.siw.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import it.uniroma3.siw.model.Brano;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.repository.CommentoRepository;

@Service
public class CommentoService {
	
	@Autowired CommentoRepository commentoRepository;
	
	public void save(Commento commento) {
		this.commentoRepository.save(commento);
	}
	
	public Commento getCommentoById(Long id) {
		Optional<Commento> c= this.commentoRepository.findById(id); 	
		if(c.isEmpty())						//oppure si può usare orElse(null)... vedi
			return null;
		else 
			return c.get();
	}
	
	public void deleteCommentoById(Long id) {
		this.commentoRepository.deleteById(id);
	}


}
