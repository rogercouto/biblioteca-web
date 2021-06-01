package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Exemplar;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ExemplarRepository extends JpaRepository<Exemplar, Integer> {

    @Query("SELECT e FROM Exemplar e WHERE e.livro = :livro")
    List<Exemplar> findByLivroId(@Param("livro") Livro livro);

    @Query("SELECT e FROM Exemplar e WHERE e.disponivel = :disponivel")
    Page<Exemplar> findByDisponibilidade(@Param("disponivel") boolean disponivel, Pageable pageable);

    @Query("SELECT e FROM Exemplar e WHERE e.numRegistro = :numRegistro")
    List<Exemplar> findByNumRegistro(@Param("numRegistro") Integer numRegistro);

    @Modifying
    @Query("DELETE FROM Exemplar e WHERE e.numRegistro = :numRegistro")
    int delete(@Param("numRegistro")int numRegistro);

}
