package utn.frp.comp.terceros.services;

import org.springframework.stereotype.Service;
import utn.frp.comp.terceros.model.Facultad;
import utn.frp.comp.terceros.repositorios.FacultadRepository;
import java.util.List;
import java.util.Optional;

@Service
public class FacultadService {

    private final FacultadRepository repository;

    public FacultadService(FacultadRepository repository) {
        this.repository = repository;
    }

    public List<Facultad> findAll() {
        return repository.findAll();
    }

    public Optional<Facultad> findById(Long id) {
        return repository.findById(id);
    }

    public Facultad save(Facultad facultad) {
        return repository.save(facultad);
    }
}
