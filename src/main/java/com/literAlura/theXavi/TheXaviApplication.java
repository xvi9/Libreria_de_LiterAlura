package com.literAlura.theXavi;

import com.literAlura.theXavi.Libreria.Librery;
import com.literAlura.theXavi.repositoryo.IAutorRepositorio;
import com.literAlura.theXavi.repositoryo.ILibroRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TheXaviApplication implements CommandLineRunner {

	@Autowired
	private ILibroRepositorio libroRepositorio;
	@Autowired
	private IAutorRepositorio autorRepositorio;

	public static void main(String[] args) {
		SpringApplication.run(TheXaviApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Librery librery = new Librery(libroRepositorio, autorRepositorio);
		librery.consumo();
	}
}
