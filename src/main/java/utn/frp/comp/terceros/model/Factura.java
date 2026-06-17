package utn.frp.comp.terceros.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "facturas")
public class Factura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nroFactura; // Ej: "0001-00001234"
    @Column(name= "fecha_factura" ,nullable = false)
    private LocalDate fecha;
    
    // Aquí definimos los datos del ítem único actual
    private String descripcionItem;
    private Double precioUnitario;
    private Integer cantidad;
    private Double total;

    // LA CLAVE: Relación con Tercero (Muchos facturas pertenecen a un Tercero)
    @ManyToOne(fetch = FetchType.EAGER) // <-- Cambiá LAZY por EAGER
    @JoinColumn(name = "id_tercero", nullable = false)
    private Tercero tercero;

    // --- CONSTRUCTORES ---
    public Factura() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNroFactura() { return nroFactura; }
    public void setNroFactura(String nroFactura) { this.nroFactura = nroFactura; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public String getDescripcionItem() { return descripcionItem; }
    public void setDescripcionItem(String descripcionItem) { this.descripcionItem = descripcionItem; }

    public Double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }

    public Integer getCantidad() { return cantidad; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }

    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }

    public Tercero getTercero() { return tercero; }
    public void setTercero(Tercero tercero) { this.tercero = tercero; }
}
