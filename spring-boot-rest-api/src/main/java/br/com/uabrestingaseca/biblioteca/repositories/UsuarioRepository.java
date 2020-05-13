package br.com.uabrestingaseca.biblioteca.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.uabrestingaseca.biblioteca.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer>{

    @Query("SELECT u FROM Usuario u WHERE u.email = :email")
    Usuario findByUsername(@Param("email") String email);

    @Modifying
    @Query("DELETE FROM Usuario u WHERE u.id = :id")
    int delete(@Param("id")int id);

}
