package com.aluracursos.bookstorage.repository;

import com.aluracursos.bookstorage.model.Language;
import com.aluracursos.bookstorage.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepositoryLibro extends JpaRepository<Libro, Long>  {

   List<Libro> findAll();

   List<Libro> findTop10ByOrderByNumeroDeDescargaDesc();

   List<Libro> findByLenguaje(Language lenguaje);

  @Query("SELECT COUNT(l) FROM Libro l WHERE l.lenguaje = :lenguaje")
  Long contarLibrosPorLenguaje(@Param("lenguaje") Language lenguaje); // cambiar lenguaje por lenguajes

   @Query("SELECT l FROM Libro l WHERE l.autor.Id = :idAutor")
  List<Libro> findLibrosByAutorId(@Param("idAutor") Long idAutor);
}
