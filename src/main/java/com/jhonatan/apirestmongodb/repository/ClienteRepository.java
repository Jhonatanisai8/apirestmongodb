package com.jhonatan.apirestmongodb.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.jhonatan.apirestmongodb.documents.Cliente;

@Repository
public interface ClienteRepository 
extends ReactiveMongoRepository<Cliente, String> {

}
