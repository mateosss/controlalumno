//clase logica que maneja el objeto nota que se encuentra dentro del objeto historial
package com.example.nav;

import java.io.Serializable;
import java.util.Date;

public class Nota implements Serializable{

    private Date fecha;
    private int nota;
    private String anotacion;
    private int id;

    public Nota(Date fecha, int nota, String anotacion) {
        this.fecha = fecha;
        this.nota = nota;
        this.anotacion = anotacion;
    }

    @Override
    public String toString() {
        return "Nota "+nota+"\nAnotacion "+anotacion+"\nfecha "+fecha.toString()+"\nID "+id;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public String getAnotacion() {
        return anotacion;
    }

    public void setAnotacion(String anotacion) {
        this.anotacion = anotacion;
    }
}