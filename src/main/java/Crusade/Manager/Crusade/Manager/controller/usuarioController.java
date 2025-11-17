package Crusade.Manager.Crusade.Manager.controller;

import Crusade.Manager.Crusade.Manager.model.usuarioModel;
import Crusade.Manager.Crusade.Manager.service.usuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class usuarioController {

    @Autowired
    private usuarioService service;

    @GetMapping
    public List<usuarioModel> getAll() {
        return service.getAll();
    }

    @PostMapping
    public usuarioModel save(@RequestBody usuarioModel usuario) {
        return service.save(usuario);
    }
}
