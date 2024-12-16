package com.aluracursos.bookstorage.principal;

import com.aluracursos.bookstorage.model.*;
import com.aluracursos.bookstorage.repository.RepositoryAutor;
import com.aluracursos.bookstorage.repository.RepositoryLibro;
import com.aluracursos.bookstorage.service.ConsumoAPI;
import com.aluracursos.bookstorage.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;


public  class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<Libro>  Libros;
    private List<Autor> autores;

    private RepositoryLibro libroRepository;
    private RepositoryAutor autorRepository;

    public Principal(RepositoryLibro libroRepository, RepositoryAutor autorRepository){
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void muestraElMenu() {

        var opcion = -1;
        while(opcion !=0){

            var menu = """
             
                                MENU DE OPCIONES
                                        
            1. Busca el libro en el índice Gutembert y lo ingresa a la base de datos si no está
            2. Consulta los libros de  la base de datos
            3. Consulta los autores registrados en la base de datos.
            4, Consulta de libros por idioma, en la base de datos.
            5. Consultar los autores vivos en un año específico.
            6. Muestra los libros mas descargados 
            7. Muestra las descripciones de los libros de la de la base de datos
            8. lista autores vivos en un año específico
                 
            0. Salir     
            """;

            System.out.println(menu);
            String menuOpciones = teclado.nextLine();
            try {
                opcion = parseInt(menuOpciones);
            }catch (NumberFormatException e){
                System.out.println("´POR FAVOR INGRESE UN VALOR NUMERICO");
                continue;
            }

            switch (opcion) {
                case 1:
                    escribeLibroWeb();
                    break;
                case 2:
                    consultaLIbrosDeLaDB();
                    break;
                case 3:
                     ConsultaAutoresDeLaBD();
                     break;




                case 4:
                     buscarIdioma();
                     break;
                case 5:
                     AutoresVivosEnAño();
                     break;
                case 6:
                    topDescargas();
                     break;
                case 7:
                     descripcionLlibros();
                     break;
                case 0:
                    //System.out.println("... Saliendo del programa ... ");
                     break;
                default:
                    System.out.println("DIGITÓ UNA OPCION INCORRECTA");
            }
        }
    }

    // obtiene el libro de Gutendex
    private DatosLibros getDatosLibros() {

        System.out.print("Escribe el nombre del libro que quieres buscar ");
        var libroBuscado = teclado.nextLine();
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + libroBuscado.replace(" ", "+"));
        System.out.println("DATOS Json----> " + json);
        var datosBusqueda = conversor.obtenerDatos(json, DatosBiblioteca.class);
        return datosBusqueda.listaLibros().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(libroBuscado.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    public void escribeLibroWeb() {
        DatosLibros datos = getDatosLibros();

        System.out.println("DATOS DE LIBROS ---->" + datos);

        if (datos == null) {
            System.out.println("El libro no existe en Gutendex ");
            return;
        }
        List<Autor> autores = datos.autor().stream()
                .map(datosAutor -> autorRepository.findByNombreAutor(datosAutor.nombreAutor())
                        .orElseGet(() -> {

                            Autor nuevoAutor = new Autor();
                            nuevoAutor.setNombreAutor(datosAutor.nombreAutor());
                            nuevoAutor.setAnioNacimiento(datosAutor.anioNacimiento());
                            nuevoAutor.setAnioFallecimiento(datosAutor.anioFallecimiento());
                            autorRepository.save(nuevoAutor);
                            return nuevoAutor;
                        })
                ).collect(Collectors.toList());

        try {
            Libro libro = new Libro(datos);
            libro.setAutor(autores.get(0));
//            libro.setAutor(autores.get(0))
            libroRepository.save(libro);
            System.out.println("EL LIBRO GUARDADO EN LA BASE DE DATOS ES: " + datos);


        } catch(DataIntegrityViolationException e) {
            System.out.println("""
                                        
                       ¡EL LIBRO  YA ESTA REGISTRADO EN LA BD!:
                   
                    """+datos);
        }
    }

    private void consultaLIbrosDeLaDB(){

        List<Libro> todosLosLibros = libroRepository.findAll();

//            todosLosLibros.stream()
//                .sorted(Comparator.comparing(Libro::toString))
//                .forEach(System.out::println);
        System.out.println("\n\n LIBROS DE LA BASE DE DATOS: \n");
            todosLosLibros.forEach(s -> System.out.println(
                    """ 
                    Titulo : %s - Autor: %s  -Idiomas: %s  -Descargas %s  -Descripción:  %s   
                    """.formatted( s.getTitulo(), s.getAutor(), s.getLenguaje(), s.getNumeroDeDescarga(), s.getDetallesLibro()
                        )));
    }

    private void ConsultaAutoresDeLaBD(){

     // List<Autor> autores = autorRepository.findAll();
        List<Autor> autores = autorRepository.findAllAutorWithLibro();

        System.out.println("\n\n AUTORES DE LA BASE DE DATOS: \n");
        autores.stream()
                .sorted(Comparator.comparing(Autor::toString))
                .forEach(System.out::println);
    }



    private void BuscarPorAutor(){

//        System.out.println("Ingresa el nombre del author a encontrar: ");
//        var nameAuthor = lectura.nextLine();
//        Optional<Author> author = authorRepository.findAuthors(nameAuthor);
//        if (author.isPresent()) {
//            Author foundAuthor = author.get();
//            System.out.println(formatAuthor(foundAuthor));
//        } else {
//            System.out.println("No se encontró ningún resultado para el autor: " + nameAuthor);
//        }

    }

    private void buscarIdioma(){

       System.out.println("Por favor digita el idioma de los libros que desea consultar :");
       var idioma = teclado.nextLine();
       var language =  Language.fromTotalString(idioma.toLowerCase());

       Long cantidadLibrosPorIdioma = libroRepository.contarLibrosPorLenguaje(language);
       System.out.println("\n\n CANTIDAD DE LIBROS EN " + idioma + " : " + cantidadLibrosPorIdioma +"\n\n");

       List<Libro> idiomaLibros = libroRepository.findByLenguaje(language);
       System.out.println("\n\n TITULOS DE LOS  LIBROS EN " + idioma + " REGISTRADOS EN LA BASE DE DATOS:");
       idiomaLibros.forEach(libro -> System.out.println(" - "+libro.getTitulo()));
    }

    private void AutoresVivosEnAño(){
        System.out.println(" Para ver los autores vivos, por favor digita el año:  ");
        var anioBuscado = teclado.nextInt();
        teclado.nextLine();

        //List<Autor> autoresVivos = autorRepository.autoresVivosEnAño(busquePorFecha);

        //List<Autor> autoresVivos = autorRepository.findByAnioNacimientoLessThanEqualOrAnioFallecimientoGreaterThanEqual(busquePorFecha);

       // List<Autor> autoresVivos = autorRepository. findBetweenAnioNacimientoAndAnioFallecimiento(busquePorFecha);

        List<Autor> autoresVivos= autorRepository.findAutoresByAnio(anioBuscado);

        System.out.println("\n\nAUTORES VIVOS EN "+ anioBuscado);
        autoresVivos.forEach(s-> System.out.println(
                """
                 %s - Fecha nacimiento: %s  - Fecha fallecimiento: %s               
                """
                  .formatted(s.getNombreAutor(), s.getAnioNacimiento(), s.getAnioFallecimiento())
        ));
    }

    private void topDescargas() {

        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDeDescargaDesc();
        topLibros.forEach(s -> System.out.println(
                """
                
                %S  - DESCARGAS: %s                
                """
                    .formatted(s.getTitulo(), s.getNumeroDeDescarga())));
    }

    private void descripcionLlibros(){
        List<Libro> descripcion =libroRepository.findAll();

        descripcion.forEach(s->
                System.out.println(
                """ 
                    
                    LIBRO: %s  -  %s
                   """.formatted(s.getTitulo(), s.getDetallesLibro())));
    }
}
