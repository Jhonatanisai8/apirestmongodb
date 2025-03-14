package com.jhonatan.apirestmongodb.services;

import com.jhonatan.apirestmongodb.documents.Cliente;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

// Flux<T> → Representa un flujo de múltiples elementos, similar a una lista reactiva.
// Mono<T> → Representa un único elemento o vacío.
public interface ClienteService {
    Flux<Cliente> findAll();

    Mono<Cliente> findById(String clienteId);

    Mono<Cliente> save(Cliente cliente);

    Mono<Void> delete(Cliente cliente);

}
