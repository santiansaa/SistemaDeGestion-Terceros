package utn.frp.comp.terceros.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="terceros")
public class Tercero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tercero")
    private Long id;

    @Column(name="nombre", length=70, nullable=false)
    private String nombre;

    @Column(name="cuitl", length=50, nullable=false)
    private String cuit;

    @Column(name="sitiva", length=50, nullable=false)
    private String sitiva;

    @Column(name="direccion", length=70, nullable=false)
    private String direccion;

    @Column(name="localidad", length=70)
    private String localidad;

    @Column(name="provincia", length=20)
    private String provincia;

    @Column(name="telefonos", length=120)
    private String telefonos;

    @Column(name="saldo_apertura")
    private Double saldoApertura;



    @Column(name="tipo_saldo", length=8)
    private String tipoSaldo;

    // Getters y Setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCuit() {
        return cuit;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }

    public String getSitiva() {
        return sitiva;
    }
    public void setSitiva(String sitiva) {
        this.sitiva = sitiva;
    }

    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }
    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }
    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getTelefonos() {
        return telefonos;
    }
    public void setTelefonos(String telefonos) {
        this.telefonos = telefonos;
    }

    public Double getSaldoApertura() {
        return saldoApertura;
    }
    public void setSaldoApertura(Double saldoApertura) {
        this.saldoApertura = saldoApertura;
    }

    public String getTipoSaldo() {
        return tipoSaldo;
    }
    public void setTipoSaldo(String tipoSaldo) {
        this.tipoSaldo = tipoSaldo;
    }
}

