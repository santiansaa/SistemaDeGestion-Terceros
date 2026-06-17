package utn.frp.comp.terceros.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pagos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pagos") // <-- CLAVE: Le avisamos a Hibernate que tu clave primaria es "id_pagos"
    private Long id;

    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fecha;

    @Column(name = "monto_pago", nullable = false)
    private Double importe;

    @Column(name = "modo_pago", length = 50)
    private String formaPago; 

    // Relación con Tercero 
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tercero", nullable = false) // <-- CORREGIDO: Apunta a "id_tercero" que es tu FK real en Postgres
    private Tercero tercero;

    // --- CONSTRUCTORES ---
    public Pagos() {}

    // --- GETTERS Y SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Double getImporte() { return importe; }
    public void setImporte(Double importe) { this.importe = importe; }

    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }

    public Tercero getTercero() { return tercero; }
    public void setTercero(Tercero tercero) { this.tercero = tercero; }
}
