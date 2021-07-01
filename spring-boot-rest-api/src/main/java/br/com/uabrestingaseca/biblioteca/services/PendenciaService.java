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

    public Pendencia findById(Integer id){
        return repository.findById(id).orElse(null);
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

    public Page<Pendencia> findFromUsuario(Usuario usuario, Boolean ativas, Pageable pageable){
        if (ativas != null && ativas.booleanValue()){
            return repository.findAtivasPageFromUsuario(usuario, pageable);
        }else if (ativas != null && !ativas.booleanValue()){
            return repository.findInativasPageFromUsuario(usuario, pageable);
        }else{
            return repository.findPageFromUsuario(usuario, pageable);
        }
    }

    public Page<Pendencia> findFromUsuario(Usuario usuario, Pageable pageable){
        return repository.findPageFromUsuario(usuario, pageable);
    }


}
