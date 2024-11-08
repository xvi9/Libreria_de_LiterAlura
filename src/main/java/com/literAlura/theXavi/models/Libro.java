package com.literAlura.theXavi.models;
import com.literAlura.theXavi.dats.Genero;
import com.literAlura.theXavi.models.records.DatosLibro;
import jakarta.persistence.*;

import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long libroId;

    @Column(unique = true)
    private String titulo;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)// Asegura que el autor se guarde automáticamente
    @JoinColumn(name = "autor_id")
    private Autor autor;

    @Enumerated(EnumType.STRING)
    private Genero genero;
    private String idioma;
    private Long cantidadDescargas;

    public Libro() {
    }

    public Libro(DatosLibro datosLibro) {
        this.libroId = datosLibro.libroId();
        this.titulo = datosLibro.titulo();

        if (datosLibro.autor() != null && !datosLibro.autor().isEmpty()) {
            this.autor = new Autor(datosLibro.autor().get(0));
        } else {
            this.autor = null; // o maneja el caso de que no haya autor
        }
        this.genero = generoModificado(datosLibro.genero());
        this.idioma = idiomaModificado(datosLibro.idioma());
        this.cantidadDescargas = datosLibro.cantidadDescargas();
    }

    public Libro(Libro libro) {
    }


    private Genero generoModificado(List<String> generos) {
        if (generos == null || generos.isEmpty()) {
            return Genero.DESCONOCIDO;
        }
        return generos.stream()
                .map(g -> {
                    int index = g.indexOf("--");
                    return index != -1 ? g.substring(index + 2).trim() : g;
                })
                .filter(Objects::nonNull)
                .findFirst()
                .map(Genero::fromString)
                .orElse(Genero.DESCONOCIDO);
    }


//    private Genero generoModificado(List<String> generos) {
//        if (generos == null || generos.isEmpty()) {
//            return Genero.DESCONOCIDO;
//        }
//        Optional<String> firstGenero = generos.stream()
//                .map(g -> {
//                    int index = g.indexOf("--");
//                    return index != -1 ? g.substring(index + 2).trim() : null;
//                })
//                .filter(Objects::nonNull)
//                .findFirst();
//        return firstGenero.map(Genero::fromString).orElse(Genero.DESCONOCIDO);
//    }

    private String idiomaModificado(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "Desconocido";
        }
        return idiomas.get(0);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

//    public String getImagen() {
//        return imagen;
//    }

//    public void setImagen(String imagen) {
//        this.imagen = imagen;
//    }

    public Long getLibroId() {
        return libroId;
    }

    public void setLibroId(Long libroId) {
        this.libroId = libroId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }


    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }


    public Long getCantidadDescargas() {
        return cantidadDescargas;
    }

    public void setCantidadDescargas(Long cantidadDescargas) {
        this.cantidadDescargas = cantidadDescargas;
    }


    @Override
    public String toString() {
        System.out.println("Libro buscado y encontrado");
        return String.format("Libro{id=%d, libroId=%d, titulo='%s', autor='%s', genero='%s', idioma='%s', cantidadDescargas=%d}",
                id, libroId, titulo,
                (autor != null ? autor.getNombre() : "Autor desconocido"),
                (genero != null ? genero : "Género desconocido"),
                (idioma != null ? idioma : "Idioma desconocido"),
                (cantidadDescargas != null ? cantidadDescargas : 0));
    }

}
