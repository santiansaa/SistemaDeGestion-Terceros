package utn.frp.comp.terceros.services;

import org.springframework.stereotype.Service;
import utn.frp.comp.terceros.model.Factura;
import utn.frp.comp.terceros.repositorios.FacturaRepository;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final FacturaRepository repository;

    public FacturaService(FacturaRepository repository) {
        this.repository = repository;
    }

    public List<Factura> findAll() { return repository.findAll(); }
    public Optional<Factura> findById(Long id) { return repository.findById(id); }
    public void deleteById(Long id) { repository.deleteById(id); }

    public Factura save(Factura factura) {
        // Aprovechamos el servicio para calcular el total antes de guardar
        if (factura.getPrecioUnitario() != null && factura.getCantidad() != null) {
            factura.setTotal(factura.getPrecioUnitario() * factura.getCantidad());
        }
        return repository.save(factura);
    }
}
