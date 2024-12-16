package com.aluracursos.bookstorage.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    @Enumerated(EnumType.STRING)
    private Language lenguaje;

    private String temas;
    private Integer numeroDeDescarga;
    private String detallesLibro;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;





//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
//    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
//    @JoinTable(
//            name= "libro_autor",
//            joinColumns = @JoinColumn(name = "libro_id"),
//            inverseJoinColumns = @JoinColumn(name = "author_id")
//    )
//    private List<Autor> autor = new ArrayList<>();



    public Libro(){}

    public Libro(DatosLibros datosLibro){
        this.titulo = datosLibro.titulo();
        this.autor = autor;

        if (!datosLibro.lenguajes().isEmpty()){
            this.lenguaje = Language.fromString(datosLibro.lenguajes().get(0));
        }else{
            throw new IllegalArgumentException(("EL IDIOMA NO ES VÁLIDO"));
        }
        this.detallesLibro = String.join(",", datosLibro.detallesLibro());
        this.numeroDeDescarga = datosLibro.numeroDescargas();
    }


    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public Language getLenguaje() {
        return lenguaje;
    }

    public Integer getNumeroDeDescarga() {
        return numeroDeDescarga;
    }

    public String getDetallesLibro() {
        return detallesLibro;
    }


    public void setDetallesLibro(String detallesLibro) {
        this.detallesLibro = detallesLibro;
    }

    public void setNumeroDeDescarga(Integer numeroDeDescarga) {
        this.numeroDeDescarga = numeroDeDescarga;
    }

    public String getTemas() {
        return temas;
    }

    public void setTemas(String temas) {
        this.temas = temas;
    }

    public void setLenguaje(Language lenguaje) {
        this.lenguaje = lenguaje;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return """
                Título del libro: %s
                Autor del libro : %s
                idioma del libro: %f
                """
                .formatted(titulo, autor, lenguaje, numeroDeDescarga);
    }
}



