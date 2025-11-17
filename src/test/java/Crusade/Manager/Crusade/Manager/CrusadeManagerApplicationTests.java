package Crusade.Manager.Crusade.Manager;


import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.repository.usuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class CrusadeManagerApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
    private usuarioRepository usuarioRepository;

	@Test
	void testUserGuardar() {
		// Arrange - Crear usuario de prueba
        usuarioModel usuario = new usuarioModel();
        usuario.setName("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
        usuario.setPassword("123456");
        usuario.setAvatar("https://via.placeholder.com/150");

        // Act - Guardamos el usuario
        usuarioModel guardado = usuarioRepository.save(usuario);

        // Assert - Verificamos que se asignó un ID y se guardaron los datos
        assertThat(guardado.getId()).isNotNull();
        assertThat(guardado.getName()).isEqualTo("Juan Pérez");
        assertThat(guardado.getEmail()).isEqualTo("juan.perez@example.com");
	}

}
