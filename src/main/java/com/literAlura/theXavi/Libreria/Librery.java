package com.literAlura.theXavi.Libreria;

import com.literAlura.theXavi.configura.ConsumoApi;
import com.literAlura.theXavi.configura.ConvertirDatos;
import com.literAlura.theXavi.models.Autor;
import com.literAlura.theXavi.models.Libro;
import com.literAlura.theXavi.models.LibroRespApi;
import com.literAlura.theXavi.models.records.DatosLibro;
import com.literAlura.theXavi.repositoryo.IAutorRepositorio;
import com.literAlura.theXavi.repositoryo.ILibroRepositorio;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

public class Librery {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConvertirDatos convertir = new ConvertirDatos();
    private static String API_BASE = "https://gutendex.com/books/?search=";
    private List<Libro> datosLibro = new ArrayList<>();
    private ILibroRepositorio libroRepository;
    private IAutorRepositorio autorRepository;
    public Librery(ILibroRepositorio libroRepository, IAutorRepositorio autorRepository) {
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public void consumo(){
        var opcion = -1;
        while (opcion != 0){
            var menu = """
                    
                    |***************************************************|
                    |*****    BIENVENIDO A LIBRERIA THE BART      ******|
                    |***************************************************|
                    
                    1 - Agregar Libro por Nombre
                    2 - Libros buscados
                    3 - Buscar libro por Nombre
                    4 - Buscar todos los Autores de libros buscados
                    5 - Buscar Autores por año
                    6 - Buscar Libros por Idioma
                    7 - Top 10 Libros mas Descargados
                    8 - Buscar Autor por Nombre
                   
                    
               
                    0 - Salir
                    
                    |***************************************************|
                    |*****       INGRESE UNA OPCIÓN VALIDA        ******|
                    |***************************************************|
                    """;

            try {
                System.out.println(menu);
                opcion = teclado.nextInt();
                teclado.nextLine();
            } catch (InputMismatchException e) {

                System.out.println("|****************************************|");
                System.out.println("|  POR FAVOR, INGRESA UN NUMERO VALIDO.  |");
                System.out.println("|****************************************|\n");
                teclado.nextLine();
                continue;
            }



            switch (opcion){
                case 1:
                    buscardorDeLibrosEnWeb();
                    break;
                case 2:
                    librosBuscados();
                    break;
                case 3:
                    buscarLibroPorNombre();
                    break;
                case 4:
                    BuscarAutores();
                    break;
                case 5:
                    buscarAutoresPorAnio();
                    break;
                case 6:
                    buscarLibrosPorIdioma();
                    break;
                case 7:
                    top10LibrosMasDescargados();
                    break;
                case 8:
                    buscarAutorPorNombre();
                    break;
                case 0:
                    opcion = 0;
                    System.out.println("|********************************|");
                    System.out.println("|  APLICACION CERRADA. GRACIAS¡  |");
                    System.out.println("|********************************|\n");
                    break;
                default:
                    System.out.println("|*********************|");
                    System.out.println("|  OPCION INCORRECTA. |");
                    System.out.println("|*********************|\n");
                    System.out.println("INTENTE CON UNA NUEVA OPCCION");
                    consumo();
                    break;
            }
        }
    }

    private Libro getDatosLibro(){
        System.out.println("Ingrese el Nombre del Libro que Quieres Encontar: ");
        var nombreLibro = teclado.nextLine().toLowerCase();
        var json = consumoApi.obtenerDatos(API_BASE + nombreLibro.replace(" ", "%20"));
        LibroRespApi datos = convertir.convertirDatosJsonAJava(json, LibroRespApi.class);

        if (datos != null && datos.getResultadoLibros() != null && !datos.getResultadoLibros().isEmpty()) {
            DatosLibro primerLibro = datos.getResultadoLibros().get(0);
            return new Libro(primerLibro);
        } else {
            System.out.println("No se encontraron resultados En Su Busqueda.");
            return null;
        }
    }

    private void buscardorDeLibrosEnWeb() {
        Libro libro = getDatosLibro();

        if (libro == null){
            System.out.println("Libro no Encontrado. por favor Intente Nuevamente");
            return;
        }

        try{
            boolean libroExists = libroRepository.existsByTitulo(libro.getTitulo());
            if (libroExists){
                System.out.println("El libro ya existe en la base de datos!");
            }else {
                libroRepository.save(libro);
                System.out.println(libro.toString());
            }
        }catch (InvalidDataAccessApiUsageException e){
            System.out.println("No se puede persisitir el libro buscado!");
        }
    }

    @Transactional(readOnly = true)
    private void librosBuscados(){
        //datosLibro.forEach(System.out::println);
        List<Libro> libros = libroRepository.findAll();
        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println("Libros encontrados en la base de datos:");
            for (Libro libro : libros) {
                System.out.println(libro.toString());
            }
        }
    }

    private void buscarLibroPorNombre() {
        System.out.println("Ingrese Titulo libro que quiere buscar: ");
        var titulo = teclado.nextLine();
        Libro libroBuscado = libroRepository.findByTituloContainsIgnoreCase(titulo);
        if (libroBuscado != null) {
            System.out.println("El libro buscado fue: " + libroBuscado);
        } else {
            System.out.println("El libro con el titulo " + titulo + " no se encontró.");
        }
    }

    private  void BuscarAutores(){
        //LISTAR AUTORES DE LIBROS BUSCADOS
        List<Autor> autores = autorRepository.findAll();

        if (autores.isEmpty()) {
            System.out.println("No se encontraron los Autores en la base de datos. \n");
        } else {
            System.out.println("Lista de Autores encontrados en la base de datos: \n");
            Set<String> autoresUnicos = new HashSet<>();
            for (Autor autor : autores) {

                if (autoresUnicos.add(autor.getNombre())){
                    System.out.println(autor.getNombre());
                }
            }
        }
    }

    private void  buscarLibrosPorIdioma(){
        System.out.println("Ingrese Idioma en el que quiere buscar: \n");
        System.out.println("|***********************************|");
        System.out.println("|  Opción - ES : Libros en español. |");
        System.out.println("|  Opción - EN : Libros en ingles.  |");
        System.out.println("|***********************************|\n");

        var idioma = teclado.nextLine();
        List<Libro> librosPorIdioma = libroRepository.findByIdioma(idioma);

        if (librosPorIdioma.isEmpty()) {
            System.out.println("No se encontraron libros en la base de datos.");
        } else {
            System.out.println(" Todos los Libros encontrados segun su Idioma :");
            for (Libro libro : librosPorIdioma) {
                System.out.println(libro.toString());
            }
        }

    }

    private void buscarAutoresPorAnio() {
//        //BUSCAR AUTORES POR ANIO

        System.out.println("Indica el año para consultar que autores estaban vivos: \n");
        var anioBuscado = teclado.nextInt();
        teclado.nextLine();

        List<Autor> autoresVivos = autorRepository.findByCumpleaniosLessThanOrFechaFallecimientoGreaterThanEqual(anioBuscado, anioBuscado);

        if (autoresVivos.isEmpty()) {
            System.out.println("No se encontraron autores que estuvieran vivos en el año " + anioBuscado + ".");
        } else {
            System.out.println("Auores que Estaban Vivos en el Año " + anioBuscado + " Son:");
            Set<String> autoresUnicos = new HashSet<>();

            for (Autor autor : autoresVivos) {
                if (autor.getCumpleanios() != null && autor.getFechaFallecimiento() != null) {
                    if (autor.getCumpleanios() <= anioBuscado && autor.getFechaFallecimiento() >= anioBuscado) {
                        if (autoresUnicos.add(autor.getNombre())) {
                            System.out.println("Autor: " + autor.getNombre());
                        }
                    }
                }
            }
        }
    }


    private void top10LibrosMasDescargados() {
        System.out.println("Los 10 Mejores Libros Encontrados en la Base de Datos");
        List<Libro> top10Libros = libroRepository.findTop10Libros(PageRequest.of(0, 10));
        if (!top10Libros.isEmpty()) {
            int index = 1;
            for (Libro libro : top10Libros) {
                System.out.println("Libro: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "N/A"));
                System.out.println("Género: " + (libro.getGenero() != null ? libro.getGenero() : "Desconocido"));
                System.out.println("Idioma: " + (libro.getIdioma() != null ? libro.getIdioma() : "Desconocido"));
                System.out.println("Descargas: " + (libro.getCantidadDescargas() != null ? libro.getCantidadDescargas() : 0));
                index++;
            }
        } else {
            System.out.println("No se encontraron libros.");
        }
    }


    private void buscarAutorPorNombre() {
        System.out.println("Ingrese nombre del escritor que deseas  encontrar: ");
        var escritor = teclado.nextLine();
        Optional<Autor> escritorBuscado = autorRepository.findFirstByNombreContainsIgnoreCase(escritor);
        if (escritorBuscado != null) {
            System.out.println("\nEl escritor buscado fue: " + escritorBuscado.get().getNombre());

        } else {
            System.out.println("\nEl escritor con el titulo " + escritor + " no se encontró.");
        }
    }

}
