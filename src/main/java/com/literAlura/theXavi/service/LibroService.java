package com.literAlura.theXavi.service;

import com.literAlura.theXavi.models.Libro;
import com.literAlura.theXavi.repositoryo.ILibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class LibroService {


    @Autowired
    private ILibroRepositorio libroRepository;

    public List<Libro> obtenerTop10Libros() {
        Pageable pageable = PageRequest.of(0, 10); // Limita a los primeros 10 libros
        return libroRepository.findTop10Libros(pageable);
    }
//    @Autowired
//    private ILibroRepositorio libroRepository;  // Inyecta iLibroRepository (con la "i" minúscula)
//
//    public List<Libro> obtenerTop10Libros() {
//        Pageable pageable = PageRequest.of(0, 10); // Obtiene los primeros 10 libros
//        return libroRepository.findTop10ByTituloByCantidadDescargasDesc(pageable);
//    }
}
