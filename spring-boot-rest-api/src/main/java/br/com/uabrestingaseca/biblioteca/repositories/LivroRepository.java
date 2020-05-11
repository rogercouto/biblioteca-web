package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    @Query("select l from Livro l where lower(l.titulo) like %:text% or lower(l.resumo) like %:text%")
    Page<Livro> findByText(@Param("text") String text, Pageable pageable);

}
