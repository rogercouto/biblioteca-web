package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    @Query("select a from Autor a where lower(a.nome) = lower(:nome) and lower(a.sobrenome) = lower(:sobrenome)")
    List<Autor> findByNames(@Param("nome") String nome, @Param("sobrenome") String sobrenome);

}
