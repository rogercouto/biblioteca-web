package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Autor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Integer> {

    @Query("SELECT a FROM Autor a WHERE " +
            "LOWER(a.nome) = LOWER(TRIM(:nome)) AND " +
            "LOWER(a.sobrenome) = LOWER(TRIM(:sobrenome))")
    List<Autor> findByNames(@Param("nome") String nome, @Param("sobrenome") String sobrenome);

    @Query("SELECT a FROM Autor a WHERE " +
            "LOWER(a.nome) LIKE LOWER(CONCAT('%',TRIM(:text), '%')) OR " +
            "LOWER(a.sobrenome) LIKE LOWER(CONCAT('%',TRIM(:text), '%'))")
    Page<Autor> findPage(@Param("text")String text, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Autor a WHERE a.id = :id")
    int delete(@Param("id")int id);

}
