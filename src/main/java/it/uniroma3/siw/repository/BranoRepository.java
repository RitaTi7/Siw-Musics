package it.uniroma3.siw.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Brano;

public interface BranoRepository extends CrudRepository<Brano, Long> {
	
	
	@Query(value= "SELECT* FROM brano b WHERE b.id NOT IN (SELECT brani_id FROM brano_autori WHERE brano_autori.autori_id = :idAutore)", nativeQuery=true)
	public Iterable<Brano> findBraniNonInAutore(Long idAutore);
	
	@Query(value="SELECT* FROM brano b  ORDER BY b.id DESC LIMIT 3", nativeQuery = true)
	public List<Brano> getUltimiBraniInseriti(int numero);
}
