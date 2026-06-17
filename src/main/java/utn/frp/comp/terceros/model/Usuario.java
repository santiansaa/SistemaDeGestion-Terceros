package utn.frp.comp.terceros.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios") // Mapea a la tabla "usuarios" en la base de datos
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID autoincremental
    private Long id;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username; // Nombre de usuario para el login (ej: "sansa")

    @Column(name = "password", nullable = false)
    private String password; // Contraseña (en producción debería ir encriptada)

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "nombre_completo", length = 100)
    private String nombreCompleto;

    @Column(name = "activo", nullable = false)
    private boolean activo = true; // Para dar de baja lógicamente sin borrar

    @Column(name = "fecha_alta", updatable = false)
    private LocalDateTime fechaAlta;

    // --- CONSTRUCTORES ---
    public Usuario() {
    }

    // Se ejecuta automáticamente antes de insertar el registro en la BD
    @PrePersist
    protected void onCreate() {
        this.fechaAlta = LocalDateTime.now();
    }

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public LocalDateTime getFechaAlta() { return fechaAlta; }
    public void setFechaAlta(LocalDateTime fechaAlta) { this.fechaAlta = fechaAlta; }
}
