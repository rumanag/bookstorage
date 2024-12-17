package com.aluracursos.bookstorage.model;

import jakarta.persistence.*;

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



