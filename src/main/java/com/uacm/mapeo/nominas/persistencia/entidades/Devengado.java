package com.uacm.mapeo.nominas.persistencia.entidades;

import jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class Devengado implements Serializable {

    private double baseAmount;
    private double extras;

    public double getBaseAmount() { return baseAmount; }
    public void setBaseAmount(double baseAmount) { this.baseAmount = baseAmount; }

    public double getExtras() { return extras; }
    public void setExtras(double extras) { this.extras = extras; }
}
