package com.aluracursos.bookstorage.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosBiblioteca(

        @JsonAlias("results") List<DatosLibros> listaLibros
) {
}
