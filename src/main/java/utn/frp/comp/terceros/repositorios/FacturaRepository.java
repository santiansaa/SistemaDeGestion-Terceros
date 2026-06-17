package utn.frp.comp.terceros.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.frp.comp.terceros.model.Factura;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Long> {
}
