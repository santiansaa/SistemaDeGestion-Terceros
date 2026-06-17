package utn.frp.comp.terceros.services;

import org.springframework.stereotype.Service;
import utn.frp.comp.terceros.model.Usuario;
import utn.frp.comp.terceros.repositorios.UsuarioRepository;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public Usuario save(Usuario usuario) {
        return repository.save(usuario);
    }

    // Lógica para verificar las credenciales
    public Optional<Usuario> login(String username, String password) {
        return repository.findByUsername(username)
                .filter(u -> u.getPassword().equals(password) && u.isActivo());
    }
}
