package br.com.uabrestingaseca.biblioteca.repositories;

import br.com.uabrestingaseca.biblioteca.model.Assunto;
import br.com.uabrestingaseca.biblioteca.model.Livro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LivroRepository extends JpaRepository<Livro, Integer> {

    @Query("SELECT l FROM Livro l WHERE " +
            "LOWER(l.titulo) like LOWER(CONCAT('%',TRIM(:text),'%')) or " +
            "LOWER(l.resumo) like LOWER(CONCAT('%',TRIM(:text),'%')) or "+
            "LOWER(l.nomesAutores) like LOWER(CONCAT('%',TRIM(:text),'%')) or "+
            "LOWER(l.editora.nome) like LOWER(CONCAT('%',TRIM(:text),'%')) or "+
            "LOWER(l.assunto.descricao) like LOWER(CONCAT('%',TRIM(:text),'%'))")
    Page<Livro> findByText(@Param("text") String text, Pageable pageable);

    @Query("SELECT l FROM Livro l WHERE l.assunto = :assunto")
    Page<Livro> findByAssunto(@Param("assunto")Assunto assunto, Pageable pageable);

}
