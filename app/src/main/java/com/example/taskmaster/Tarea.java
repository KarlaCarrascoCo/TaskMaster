package com.example.taskmaster;

public class Tarea {

    private String nombre;
    private String descripcion;
    private String prioridad;
    private String categoria;
    private boolean completada;

    public Tarea(
            String nombre,
            String descripcion,
            String prioridad,
            String categoria,
            boolean completada) {

        this.nombre = nombre;
        this.descripcion = descripcion;
        this.prioridad = prioridad;
        this.categoria = categoria;
        this.completada = completada;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getCategoria() {
        return categoria;
    }

    public boolean isCompletada() {
        return completada;
    }

    public void setCompletada(boolean completada) {
        this.completada = completada;
    }
}