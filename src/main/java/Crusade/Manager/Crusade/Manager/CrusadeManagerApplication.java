package Crusade.Manager.Crusade.Manager;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.repository.usuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CrusadeManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrusadeManagerApplication.class, args);
	}

	//Creamos un usuario que se mantiene en memoria mientras el server esta encendido 
	@Bean
	CommandLineRunner initData(usuarioRepository usuarioRepository) {
		return args -> {
			if(usuarioRepository.count() == 0) {
				usuarioModel usuario = new usuarioModel();
				usuario.setName("Usuario H2");
				usuario.setEmail("user@user.com");
				usuario.setPassword("123456");
				usuarioRepository.save(usuario);

				System.out.println("Usuario de prueba creado en H2");
			} else {
				System.out.println("Ya hay usuarios, no se agregó uno nuevo");
			}
		};
	}
}
