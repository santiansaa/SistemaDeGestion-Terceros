package utn.frp.comp.terceros.services;

import org.springframework.stereotype.Service;
import utn.frp.comp.terceros.model.Pagos;
import utn.frp.comp.terceros.repositorios.PagosRepository;
import java.util.List;
import java.util.Optional;

@Service
public class PagosService {

    private final PagosRepository repository;

    public PagosService(PagosRepository repository) {
        this.repository = repository;
    }

    public List<Pagos> findAll() { return repository.findAll(); }
    public Optional<Pagos> findById(Long id) { return repository.findById(id); }
    public Pagos save(Pagos pago) { return repository.save(pago); }
    public void deleteById(Long id) { repository.deleteById(id); }
}
