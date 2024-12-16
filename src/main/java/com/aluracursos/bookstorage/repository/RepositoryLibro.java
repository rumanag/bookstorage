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

 // List<Libro> findTotalLibroByLanguaje(Language lenguaje);

 /* El error que estás viendo sugiere que hay un problema con el nombre del parámetro en tu consulta.
 Asegúrate de que el nombre del parámetro en la consulta coincida exactamente con el nombre del parámetro
 en el metodo. aquí hay una versión corregida de tu código

  @Query("SELECT COUNT(l) FROM Libro l WHERE l.lenguaje = :lenguaje")
  List<Libro> contarLibrosPorLenguaje(Language lenguaje);
  */

@Query("SELECT COUNT(l) FROM Libro l WHERE l.lenguaje = :lenguaje")
Long contarLibrosPorLenguaje(@Param("lenguaje") Language lenguaje); // cambiar lenguaje por lenguajes

}
