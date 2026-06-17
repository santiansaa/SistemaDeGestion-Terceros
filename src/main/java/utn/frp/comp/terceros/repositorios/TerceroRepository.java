package utn.frp.comp.terceros.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.frp.comp.terceros.model.Tercero;
@Repository
public interface TerceroRepository extends JpaRepository<Tercero, Long> {

}