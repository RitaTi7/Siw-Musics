package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Brano;
import it.uniroma3.siw.repository.BranoRepository;

@Service
public class BranoService {
	
	@Autowired BranoRepository branoRepository;
	
	public Brano getBranoById(Long id) {
		Optional<Brano> b= this.branoRepository.findById(id); 	
		if(b.isEmpty())						//oppure si può usare orElse(null)... vedi
			return null;
		else 
			return b.get();
	}
	
	public Iterable<Brano> getAllBrani(){
		return this.branoRepository.findAll();
	}
	
	public void save(Brano brano) {
		this.branoRepository.save(brano);
	}
	
	public void deleteBranoById(Long id) {
		this.branoRepository.deleteById(id);
	}
	
	public Iterable<Brano> findBraniNonInAutore(Long idAutore){
		return this.branoRepository.findBraniNonInAutore(idAutore);
	}
	
	public List<Brano> getUltimiBraniInseriti(int numero){
		return this.branoRepository.getUltimiBraniInseriti(numero);
	}
	
}
