package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Brano;

public interface BranoRepository extends CrudRepository<Brano, Long> {
	
	
	@Query(value= "SELECT* FROM brano b WHERE b.id NOT IN (SELECT brani_id FROM brano_autori WHERE brano_autori.autori_id = :idAutore)", nativeQuery=true)
	public Iterable<Brano> findBraniNonInAutore(Long idAutore);

}
