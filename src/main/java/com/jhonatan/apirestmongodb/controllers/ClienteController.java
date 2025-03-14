package com.jhonatan.apirestmongodb.controllers;

import java.io.File;
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
import com.jhonatan.apirestmongodb.services.serviceImple.ClienteServiceImpl;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteServiceImpl service;

    @Value("${config.uploads.path}")
    private String path;

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

    @PostMapping("/upload/{clienteId}")
    public Mono<ResponseEntity<Cliente>> subirFoto(@PathVariable String clienteId, @RequestPart FilePart file) {
        return service.findById(clienteId).flatMap(c -> {
            // generar nombre único para la foto
            c.setFoto(UUID.randomUUID().toString() + "-" + file.filename().replace(" ", "")
                    .replace(":", "")
                    .replace("/", ""));

            // guarda la imagen en el sistema de archivos
            return file.transferTo(new File(path + c.getFoto()))
                    .then(service.save(c)); // actualiza el cliente con la foto en la BD
        }).map(clie -> ResponseEntity.ok(clie)) // si todo ok 200
                .defaultIfEmpty(ResponseEntity.notFound().build()); // si no con eror 404
    }

    @GetMapping("/findAllClients")
    public Mono<ResponseEntity<Flux<Cliente>>> listarClientes() {

        // duelve en una sola respuesta todos los clientes
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.findAll()));
    }

    @GetMapping("/findClientById/{clienteId}")
    public Mono<ResponseEntity<Cliente>> listarClientePorId(@PathVariable String clienteId) {
        // devuelve un solo cliente en la respuesta
        return service.findById(clienteId) /* Transforma el Mono<Cliente> en un Mono<ResponseEntity<Cliente>> */
                .map(c -> ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(c))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

}
