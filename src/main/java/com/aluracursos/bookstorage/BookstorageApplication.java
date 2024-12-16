package com.aluracursos.bookstorage;

import com.aluracursos.bookstorage.repository.RepositoryAutor;
import com.aluracursos.bookstorage.repository.RepositoryLibro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.aluracursos.bookstorage.principal.Principal;


@SpringBootApplication
public class BookstorageApplication implements CommandLineRunner {

	@Autowired
	private RepositoryLibro libroRepository;
	@Autowired
	private RepositoryAutor autorRepository;

	public static void main(String[] args){
		SpringApplication.run(BookstorageApplication.class,args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(libroRepository, autorRepository);
		principal.muestraElMenu();
	}
}