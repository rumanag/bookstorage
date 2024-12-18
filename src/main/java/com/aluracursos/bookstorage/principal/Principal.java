package com.aluracursos.bookstorage.principal;

import com.aluracursos.bookstorage.model.*;
import com.aluracursos.bookstorage.repository.RepositoryAutor;
import com.aluracursos.bookstorage.repository.RepositoryLibro;
import com.aluracursos.bookstorage.service.ConsumoAPI;
import com.aluracursos.bookstorage.service.ConvierteDatos;
import org.springframework.dao.DataIntegrityViolationException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.getInteger;
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

        var opcion =-1;
        while(opcion != 0){

            var menu = """
                                          MENU DE OPCIONES
                1. Busca el libro en el índice Gutembert y lo ingresa a la base de datos, si no está.
                2. Consulta los libros de  la base de datos.
                3. Consulta de libros por idioma, en la base de datos.
                4. Muestra los libros mas descargados.
                5. Muestra las descripciones de los libros de la de la base de datos.
                6. Consulta los autores registrados en la base de datos.
                7. Consulta los libros de la base de datos, por autor.
                8. los autores vivos en un año específico.

                0. Salir.
                """;

            System.out.println(menu);
            var menuOpcion = teclado.nextLine();

            try {
                opcion = Integer.parseInt(menuOpcion.trim());

                switch (opcion) {
                    case 1:
                        escribeLibroWeb();
                        break;
                    case 2:
                        consultaLIbrosDeLaDB();
                        break;
                    case 3:
                        buscarIdioma();
                        break;
                    case 4:
                        topDescargas();
                        break;
                    case 5:
                        descripcionLlibros();
                        break;
                    case 6:
                        consultaAutoresDeLaBD();

                        break;
                    case 7:
                        consultaLibrosPorAutor();
                        break;
                    case 8:
                        findAutoresByAnio();
                        break;
                    case 0:
                        break;
                    default:
                        System.out.println("DIGITÓ UNA OPCION INCORRECTA");
                        break;
                }
            }catch (NumberFormatException e){
                System.out.println("´POR FAVOR INGRESE UN VALOR NUMERICO");
            }
        }
    }

    // A. SERVICIO: OBTIENE EL LIBRO ESCOGIDO DE Gutendex
    private DatosLibros getDatosLibros() {

        System.out.print("Escribe el nombre del libro que quieres buscar ");
        var libroBuscado = teclado.nextLine();
        var libroChequeado = libroBuscado;

        boolean tieneCaracteresEspeciales= checkSpecialCharacters(libroBuscado);

        if (tieneCaracteresEspeciales == true){
                libroChequeado= "npsel";
        }

        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + libroChequeado.replace(" ", "+"));
        System.out.println("DATOS Json----> " + json);
        var datosBusqueda = conversor.obtenerDatos(json, DatosBiblioteca.class);
        return datosBusqueda.listaLibros().stream()
                .filter(datosLibros -> datosLibros.titulo().toUpperCase().contains(libroBuscado.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

//    1. VALIDA LOS DATOS DE INGRESO Y ESCRIBE LIBRO EN LA BASE DE DATOS

    public void escribeLibroWeb() {
        DatosLibros datos = getDatosLibros();

        System.out.println("DATOS DE LIBROS ---->" + datos);

        if (datos == null) {
            System.out.println("El libro no existe en Gutendex. Por favor revise que el nombre no tenga caracteres especiales ");
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
            System.out.println("LIBRO GUARDADO EN LA BASE DE DATOS ES: " + datos);


        } catch(DataIntegrityViolationException e) {
            System.out.println("""
                       
                       ¡EL LIBRO  YA ESTA REGISTRADO EN LA BD!:
                       """+datos);
        }
    }
//     2. CONSULTA TODOS LOS LIBROS DE LA BASE DE DATOS
    private void consultaLIbrosDeLaDB(){

        List<Libro> todosLosLibros = libroRepository.findAll();

        System.out.println("\n\n LIBROS DE LA BASE DE DATOS: \n");
            todosLosLibros.forEach(s -> System.out.println(
                    """ 
                    TÍTULO : %s - %s-Idiomas: %s  -Descargas %s  -Descripción:  %s   
                    """.formatted( s.getTitulo(), s.getAutor(), s.getLenguaje(), s.getNumeroDeDescarga(), s.getDetallesLibro()
                        )));
    }

//    3. BUSCA LIBROS POR IDIOMAS, DE LA BASE DE DATOS

    private void buscarIdioma(){

        try {
            System.out.println("Por favor digita el idioma de los libros que desea consultar :");

            var idioma = teclado.nextLine();
            var language = Language.fromTotalString(idioma.toLowerCase());

            Long cantidadLibrosPorIdioma = libroRepository.contarLibrosPorLenguaje(language);
            System.out.println("\n\n CANTIDAD DE LIBROS EN " + idioma + " : " + cantidadLibrosPorIdioma + "\n\n");

            List<Libro> idiomaLibros = libroRepository.findByLenguaje(language);
            System.out.println("\n\n TITULOS DE LOS  LIBROS EN " + idioma + " REGISTRADOS EN LA BASE DE DATOS:");
            idiomaLibros.forEach(libro -> System.out.println(" - " + libro.getTitulo()));

        } catch (IllegalArgumentException e) {
            System.out.println("NO SE ENCUENTRA EL IDIOMA  ESCOGIDO: ");
        }
    }

    //    4. TOP DESCARGAS DE LA BASE DE DATOS
    private void topDescargas() {

        List<Libro> topLibros = libroRepository.findTop10ByOrderByNumeroDeDescargaDesc();

        System.out.println("                     LIBROS MAS DESCARGADOS\n\n");
        System.out.println(("    TITULO           DESCARGAS"));
        topLibros.forEach(s -> System.out.println(
                """
                %S  - %s
                """
                        .formatted(s.getTitulo(), s.getNumeroDeDescarga())));
    }

    //    5. CONSULTA LAS DESCRIPCIONES DE LOS LIBROS DE LA BASE DE DATOS
    private void descripcionLlibros(){
        List<Libro> descripcion =libroRepository.findAll();
        System.out.println("\n\n      DESCRIPCION DE LOS LIBROS\n\n");
        descripcion.forEach(s->
                System.out.println(
                        """
                        %s  -  %s
                        """.formatted(s.getTitulo(), s.getDetallesLibro())));
    }

//    6. CONSULTA AUTORES DE LA BASE DE DATOS
    private void consultaAutoresDeLaBD(){

     // List<Autor> autores = autorRepository.findAll();
        List<Autor> autores = autorRepository.findAllAutorWithLibro();

        System.out.println("\n\n AUTORES DE LA BASE DE DATOS: \n");
        autores.stream()
                .sorted(Comparator.comparing(Autor::toString))
                .forEach(System.out::println);
    }

//    7. CONSULTA LIBROS POR AUTOR, DE LA BASE DE DATOS
    private void consultaLibrosPorAutor(){

        System.out.println("Ingresa el nombre del author a encontrar: ");
        var nombre= teclado.nextLine();

        Long idAutor = autorRepository.findNombreAutor(nombre);

        if (idAutor == null){
            System.out.println("El autor no se encuentra en la base de datos. Por favor intente de nuevo");

        }else{

            List<Libro> librosAutor = libroRepository.findLibrosByAutorId(idAutor);

            System.out.println("\n\n LIBROS DE " + nombre +" \n");
            librosAutor.forEach(s -> System.out.println(
                    """ 
                    TÍTULO : %s - %s-Idiomas: %s  -Descargas %s  -Descripción:  %s   
                    """.formatted( s.getTitulo(), s.getAutor(), s.getLenguaje(), s.getNumeroDeDescarga(), s.getDetallesLibro()
                    )));
        }
    }

//    8. CONSULTA AUTORES QUE VIVIERON EN UN AÑO DETERMINADO, DE LA BASE DE DATOS
    private void findAutoresByAnio()   {
        System.out.println(" Para ver los autores vivos, por favor digita el año:  ");

        var anioDigitado = teclado.nextLine();

        try{
            int anioBuscado = Integer.parseInt(anioDigitado.trim());
            List<Autor> autoresVivos= autorRepository.findAutoresByAnio(anioBuscado);

            System.out.println("\n\nAUTORES VIVOS EN "+ anioBuscado);
            autoresVivos.forEach(s-> System.out.println(
                    """
                     %s - Fecha nacimiento: %s  - Fecha fallecimiento: %s
                    """
                            .formatted(s.getNombreAutor(), s.getAnioNacimiento(), s.getAnioFallecimiento())
            ));

        } catch (NumberFormatException e){
            System.out.println("\nEL VALOR INGRESADO NO ES UN NÚMERO VÁLIDO. Por favor, intente de nuevo\n\n");
        }
    }

//    B. SERVICIO: CHEQUEA CARACTERES ESPECIALES
    private boolean checkSpecialCharacters(String texto){

        Pattern pattern = Pattern.compile( "«\\¿+|\\?+|\\°+|\\¬+|\\|+|\\!+|\\#+|\\$+|\\%+|\\&+|\\+|\\=+|\\’+|\\¡+|\\++|\\*+|\\~+|\\[+|\\]+|\\{+|\\}+|\\^+|\\<+|\\>»" );
        Matcher matcher = pattern.matcher(texto);
        boolean caracteresEspeciales = matcher.find();
        return(caracteresEspeciales);
    }
}
