package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "nominas")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false, unique=true)
    private String reciboId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id", nullable = false)
    private Empleado empleado;

    @Column(nullable=false)
    private String periodo;

    @Embedded
    private Devengado devengado = new Devengado();

    @Embedded
    private Deducciones deducciones = new Deducciones();

    @Column(nullable=false)
    private double totalNeto;

    @Column
    private LocalDate fechaRegistro = LocalDate.now();

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getReciboId() { return reciboId; }
    public void setReciboId(String reciboId) { this.reciboId = reciboId; }

    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }

    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }

    public Devengado getDevengado() { return devengado; }
    public void setDevengado(Devengado devengado) { this.devengado = devengado; }

    public Deducciones getDeducciones() { return deducciones; }
    public void setDeducciones(Deducciones deducciones) { this.deducciones = deducciones; }

    public double getTotalNeto() { return totalNeto; }
    public void setTotalNeto(double totalNeto) { this.totalNeto = totalNeto; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
