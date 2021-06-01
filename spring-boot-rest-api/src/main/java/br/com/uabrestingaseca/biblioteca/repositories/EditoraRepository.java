package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Editora;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EditoraRepository extends JpaRepository<Editora, Integer> {

    @Query("SELECT e FROM Editora e WHERE LOWER(e.nome) = LOWER(TRIM(:nome))")
    List<Editora> findByNome(@Param("nome") String nome);

    /*
    @Query("SELECT e FROM Editora e WHERE LOWER(e.nome) = LOWER(CONCAT('%',TRIM(:text),'%'))")
    Page<Editora> find(@Param("text") String text, Pageable pageable);
    */

    @Query("SELECT e FROM Editora e WHERE " +
            "LOWER(e.nome) LIKE CONCAT('%',TRIM(LOWER(:text)),'%')")
    Page<Editora> find(@Param("text") String text, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Editora e WHERE e.id = :id")
    int delete(@Param("id")int id);

}
