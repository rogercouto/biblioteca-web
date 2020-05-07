package br.com.uabrestingaseca.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.uabrestingaseca.biblioteca.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    @Query("select u from Usuario u where u.email =:email")
    Usuario findByUsername(@Param("email") String email);

}
