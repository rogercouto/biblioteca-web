package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EditoraRepository extends JpaRepository<Editora, Integer> {

    @Query("select e from Editora e where lower(e.nome) = lower(:nome)")
    Editora findByNome(@Param("nome") String nome);

}
