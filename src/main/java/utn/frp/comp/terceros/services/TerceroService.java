package utn.frp.comp.terceros.services;

import org.springframework.stereotype.Service;
import utn.frp.comp.terceros.model.Tercero;
import utn.frp.comp.terceros.repositorios.TerceroRepository;
import java.util.List;
import java.util.Optional;

@Service
public class TerceroService {

    private final TerceroRepository repository;

    public TerceroService(TerceroRepository repository) {
        this.repository = repository;
    }

    public List<Tercero> findAll() {
        return repository.findAll();
    }

    public Optional<Tercero> findById(Long id) {
        return repository.findById(id);
    }

    public Tercero save(Tercero tercero) {
        return repository.save(tercero);
    }
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
}
