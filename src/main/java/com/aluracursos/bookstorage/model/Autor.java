package com.aluracursos.bookstorage.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "autores")
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    private String nombreAutor;
    private Integer anioNacimiento;
    private Integer anioFallecimiento;

    @OneToMany(mappedBy = "autor")
    private List<Libro> libro = new ArrayList<>();

    public Autor(){
    }

    public Autor(DatosAutor datosAutor) {
        this.nombreAutor = datosAutor.nombreAutor();
        this.anioNacimiento = datosAutor.anioNacimiento();
        this.anioFallecimiento= datosAutor.anioFallecimiento();
    }

    public List<Libro> getLibro() {
        return libro;
    }

    public void setLibro(List<Libro> libro) {
        this.libro = libro;
    }

    public long getId() {
        return Id;
    }

    public Integer getAnioNacimiento() {
        return anioNacimiento;
    }

    public void setAnioNacimiento(Integer anioNacimiento) {
        this.anioNacimiento = anioNacimiento;
    }

    public Integer getAnioFallecimiento() {
        return anioFallecimiento;
    }

    public void setAnioFallecimiento(Integer anioFallecimiento) {
        this.anioFallecimiento = anioFallecimiento;
    }

    public String getNombreAutor() {
        return nombreAutor;
    }

    public void setNombreAutor(String nombreAutor) {
        this.nombreAutor = nombreAutor;
    }

    @Override
    public String toString() {
        return """
                - %S -Nacimiento : %S -Fallecimiento : %S                       
                """
                   .formatted(nombreAutor, anioNacimiento, anioFallecimiento);
    }
}