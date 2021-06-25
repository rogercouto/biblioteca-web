package br.com.uabrestingaseca.biblioteca.services;

import br.com.uabrestingaseca.biblioteca.model.Pendencia;
import br.com.uabrestingaseca.biblioteca.model.Usuario;
import br.com.uabrestingaseca.biblioteca.repositories.PendenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PendenciaService {

    @Autowired
    private PendenciaRepository repository;

    public Pendencia save(Pendencia pendencia){
        return repository.save(pendencia);
    }

    public void delete(Integer id){
        repository.findById(id).ifPresent(pendencia->{
            repository.delete(pendencia);
        });
    }

    public Page<Pendencia> findPage(Pageable pageable){
        return repository.findAll(pageable);
    }

    public Page<Pendencia> findPage(Pageable pageable, boolean ativas){
        if (ativas){
            return repository.findAtivasPage(pageable);
        }else{
            return repository.findInativasPage(pageable);
        }
    }

    public List<Pendencia> findFromUsuario(Usuario usuario, Boolean ativas){
        if (ativas != null && ativas.booleanValue() == true){
            return repository.findAtivasListFromUsuario(usuario);
        }else if (ativas != null && ativas.booleanValue() == false){
            return repository.findInativasListFromUsuario(usuario);
        }else{
            return repository.findListFromUsuario(usuario);
        }
    }


}
