/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.collabhangman.model.game;

import java.util.Date;

/**
 *
 * @author 2095957
 */
public class Score {

    private Date fechaObtencion;
    private long valorPuntaje;

    public Date getFechaObtencion() {
        return fechaObtencion;
    }

    public void setFechaObtencion(Date fechaObtencion) {
        this.fechaObtencion = fechaObtencion;
    }

    public long getValorPuntaje() {
        return valorPuntaje;
    }

    public void setValorPuntaje(long valorPuntaje) {
        this.valorPuntaje = valorPuntaje;
    }

}
