package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Deducciones implements Serializable {

    private double isr;
    private double otras;

    public double getIsr() { return isr; }
    public void setIsr(double isr) { this.isr = isr; }

    public double getOtras() { return otras; }
    public void setOtras(double otras) { this.otras = otras; }
}
