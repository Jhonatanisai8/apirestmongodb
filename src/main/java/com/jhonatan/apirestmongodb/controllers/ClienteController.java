package com.jhonatan.apirestmongodb.controllers;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import com.jhonatan.apirestmongodb.documents.Cliente;
import com.jhonatan.apirestmongodb.repository.ClienteRepository;
import com.jhonatan.apirestmongodb.services.serviceImple.ClienteServiceImpl;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @Autowired
    private ClienteServiceImpl service;

    @Value("${config.uploads.path}")
    private String path;

    ClienteController(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @PostMapping("/saveClienteWithPhoto")
    public Mono<ResponseEntity<Cliente>> saveClientePhoto(@RequestBody Cliente cliente,
            @RequestPart FilePart file) {

        // guarda un nombre unico de para la foto
        cliente.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                .replace(" ", "")
                .replace(":", "")
                .replace("/", ""));

        // guardamos el archivo y luego guardamos el cliente
        return file.transferTo(new File(path + cliente.getFoto()))
                .then(service.save(cliente))
                .map(clie -> ResponseEntity.created(URI.create("/api/clientes".concat(clie.getClienteId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(clie));
    }


}
