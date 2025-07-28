package it.uniroma3.siw.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import it.uniroma3.siw.model.Autore;

public interface AutoreRepository extends CrudRepository<Autore, Long> {
	
	
	@Query(value= "SELECT* FROM autore a WHERE a.id NOT IN (SELECT autori_id FROM brano_autori WHERE brano_autori.brani_id = :branoId)", nativeQuery=true)
	public Iterable<Autore> findAutoriNonInBrano(@Param("branoId") Long id);
}
