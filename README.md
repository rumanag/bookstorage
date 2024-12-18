
#_______________________________
![vitrina](/Tools/Images/frankestein1.png) 

# BOOKSTORAGE PARA  UNA BOOK STORE   EN  LITERALURA
#### Proyecto/Desafío de ALURA-LATAM y ORACLE-ONE en el curso Especializacion Back-End.
         NOTA SOBRE EL ARCHIVO README: El documento intenta ser un "paper" que presenta una información general para quien
                                       quiera darle una mirada al proyecto.



## INTRODUCCCIÓN

El proyecto/desafío de LITERALURA fué concebido por Alura-Latam y Oracle-One para afianzar 
el conocimiento de los estudiantes de la especialización back-end, particularmente
en lo concerniente a "realizar solicitudes a una API de libros, a manipular datos JSON, 
guardarlos en una base de datos y, finalmente, a filtrar y mostrar los libros y autores 
de interés." Tal como lo plantearon los diseñadores del curso, en las tarjetas de Trello,
los objetivos particulares se centraron en  "... desarrollar un Catálogo de Libros que ofrezca 
interacción textual (vía consola) con los usuarios, proporcionando al menos 5 opciones 
de interacción. Los libros se buscarán a través de una API específica". 

Para nosotros los estudiantes, no ha sido un reto sencillo y, en mi caso particular, el 
tiempo gastado en el desarrollo superó las cuatro semanas, ya que fué necesario estudiar cada
bloque de código y realizar consultas,  pruebas y más pruebas hasta lograr entender la lógica
frente al código y obtener el resultado que esperado, para así pasar al siguiente bloque
y continuar el mismo ciclo. 

Así pues, el código finalmente ha sido el resultado de ensamblar muchas piezas de 
diferentes fuentes (material del curso, aporte de profesores y de compañeros, uso de la 
inteligencia artificial, documentación oficial de los programas, etc.), logrando una especie de
frankestein en código, como lo describe Mary Shelley en su famoso libro, de ahí la imagen del
inicio de este documento.
Pienso que se logró entender en un porcentaje aceptable el funcionamiento del código y las 
relaciones entre cada uno de los módulos, pero, lo que es más importante, se fundaron las
bases para continuar profundizando en el aprendizaje del mundo digital de  Java:,  sus 
frameworks, bibliotecas, APIs, Etc,

No sobra agregar nuestro reconocimiento con los profesores de los diferentes módulos, por el
aporte y apoyo que nos brindaron durante las clases, con los monitores  que nos 
guiaron en el desarrollo del proyecto (en mi caso, @julia83), con los compañeros que nos 
indicaron sus soluciones y, en general, a Alura-Oracle por su metodología educativa orientada
al logro y su excelente funcionamiento organizacional.

    

## DESCRIPCION  DEL PROYECTO

   El proyecto consiste en el desarrollo de un programa que permita consumir datos de 
   la API Gutendex. De acuerdo con el README.md del repositorio de GitHub de Gutendex, es una 
   API web en JSON que proporciona información sobre el catálogo de libros de licencia "libre"
   online del Proyecto Gutenberg. 
   Debido a su tamaño de gran escala, el proyecto Gutemberg  publica sus datos en complicados
   archivos XML, por lo cual Gutendex descarga estos archivos, los guarda en su base de datos y
   los publica en el formato sencillo de JSON (*traducción libre del sitio*).
   
   Con la anterior informacion a mano, al ejecutar el programa el usuario  puede llevar
   un registro personal , en su base de datos, de los libros y los autores que tiene disponible
   en Guntex y, por supuesto, en el proyecto Gutemberg.
   
### GUTENDEX
    
   En el siguiene enlace se encuentra la documentación completa para la instalación y uso 
   de Gutedex: https://github.com/garethbjohnson/gutendex, donde se explican los API OBJECTS
   del Json, así como los parámetros de consulta y búsqueda. 
   La estructura del Json en Gutendex es como se presenta a continuación.

    {"count":74568,"next":"https://gutendex.com/books/?page=2","previous":null,
	"results":[
				{"id":84,
				 "title":"Frankenstein; Or, The Modern Prometheus",
				 "authors":
					[
						{"name":"Shelley, Mary Wollstonecraft",
						 "birth_year":1797,
						 "death_year":1851}
					],
				"translators":
					[
					],
				"subjects":
					[
						"Frankenstein's monster (Fictitious character) -- Fiction",
						"Frankenstein, Victor (Fictitious character) -- Fiction",
						"Gothic fiction","Horror tales","Monsters -- 
						Fiction","Science fiction","Scientists -- Fiction"
					],
				"bookshelves":
					[
						"Browsing: Culture/Civilization/Society",
						"Browsing: Fiction",
						"Browsing: Gender & Sexuality Studies",
						"Browsing: Literature",
						"Browsing: Science-Fiction & Fantasy",
						"Gothic Fiction",
						"Movie Books",
						"Precursors of Science Fiction",
						"Science Fiction by Women"
					],
				"languages":
					[
						"en"
					],
				"copyright":false,
				"media_type":"Text",
				"formats":
				{
					"text/html":"https://www.gutenberg.org/ebooks/84.html.images",
					"application/epub+zip":"https://www.gutenberg.org/ebooks/84.epub3.images",
					"application/x-mobipocket-ebook":"https://www.gutenberg.org/ebooks/84.kf8.images",
					"application/rdf+xml":"https://www.gutenberg.org/ebooks/84.rdf",
					"image/jpeg":"https://www.gutenberg.org/cache/epub/84/pg84.cover.medium.jpg",
					"text/plain; charset=us-ascii":"https://www.gutenberg.org/ebooks/84.txt.utf-8",
					"application/octet-stream":"https://www.gutenberg.org/cache/epub/84/pg84-h.zip"
				},
				"download_count":174761
			}
	]
      "download_count":18938
      }

### EL PROGRAMA BOOKSTORAGE

1. **GESTIÓN DEL PROYECTO**
   El proyecto fué desarrollado con el apoyo de TRELLO, cuyas tarjetas iniciales,(con las fases, y los objetivos
   de cada fase) fueron propuestas por el grupo  Alura-Latam y Oracle-One:

![Trello](/Tools/Images/trello.png)

   Durante el desarrollo, cada tarjeta cumplió su misión de guiar cada fase, así en gran parte de ellas los tiempos
   no se cumplieron con exactitud.

2. **INFRAESTRUCTURA DE SOFTWARE**

   - El programa está desarrollado en java, con el framework SPRING,  utilizando MAVEN (dependencias),
     HIBERNATE (persistencia) y Tomcat (servidor web).
   
   - Usamos PostgreSQL para el manejo de la base de datos que llamamos  "bookstorage", con las tablas
     "libros" y "autores", como se muestra a continuación:

![tablas](/Tools/Images/tablasRelacion.png)

3. **MODELO DEL SISTEMA**

   La siguiente figura presenta el modelo general de clases, interfaces, enumerators y records (DTO) :


![caso de uso ](/Tools/Images/modelo.png)
 

### CASO DE USO
   El siguiente caso de uso permite aclarar el funcionamiento del aplicativo, desde el punto de vista
   de los actores involucrados: El usuario  y el programa dando respuesta a sus solicitudes:
![caso de uso ](/Tools/Images/casoDeUso.png)

### OTRAS CONSIDERACIONES
   - Requisitos de entorno:

         spring.application.name=bookstorage
         spring.datasource.url=jdbc:postgresql://${DB_HOST}/bookstorage
         spring.datasource.username=${DB_USER}
         spring.datasource.password=${DB_PASSWORD}
         spring.datasource.driver-class-name=org.postgresql.Driver
         hibernate.dialect=org.hibernate.dialect.HSQLDialect
         spring.jpa.hibernate.ddl-auto=update

   - Requisitos de hardware: Computador con 4gb de memoria ram, y windows compatible con 
     java 17.012 o superior.
   -  Esta es la versión 1.0 18/12/2024, y  no cuenta con derechos reservados de algún tipo.
   -  Este proyecto está alojado en el repositorio de GitHub en
      https://github.com/rumanag/bookstorage.git
   - Agradecemos sus comentarios,  observaciones y correcciones, tanto a este documento como al código del
     programa.
     
##### Cordial saludo, RICARDO UMAÑA GUZMAN






    
