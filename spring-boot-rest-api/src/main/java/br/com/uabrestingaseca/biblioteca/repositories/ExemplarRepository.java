package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {

    @Query("select e from Exemplar e where e.numRegistro = :numRegistro")
    List<Exemplar> findByNumRegistro(@Param("numRegistro") int numRegistro);

}
