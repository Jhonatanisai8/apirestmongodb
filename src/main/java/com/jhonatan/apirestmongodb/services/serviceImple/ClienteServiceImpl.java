package com.jhonatan.apirestmongodb.services.serviceImple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jhonatan.apirestmongodb.documents.Cliente;
import com.jhonatan.apirestmongodb.repository.ClienteRepository;
import com.jhonatan.apirestmongodb.services.ClienteService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClienteServiceImpl
        implements ClienteService {

    @Autowired
    private ClienteRepository repository;

    @Override
    public Mono<Void> delete(Cliente cliente) {
        return repository.delete(cliente);
    }

    @Override
    public Flux<Cliente> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<Cliente> findById(String clienteId) {
        return repository.findById(clienteId);
    }

    @Override
    public Mono<Cliente> save(Cliente cliente) {
        return repository.save(cliente);
    }

}
