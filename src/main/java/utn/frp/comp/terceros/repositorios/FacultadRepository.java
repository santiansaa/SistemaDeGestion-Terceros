package utn.frp.comp.terceros.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import utn.frp.comp.terceros.model.Facultad;
@Repository
public interface FacultadRepository extends JpaRepository<Facultad, Long> {

}
