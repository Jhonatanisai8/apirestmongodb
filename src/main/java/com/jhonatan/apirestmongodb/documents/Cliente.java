package com.jhonatan.apirestmongodb.documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Cliente {

    @Id
    private String clienteId;

    @NotEmpty
    private String nombre;

    @NotEmpty
    private String apellido;

    @NotNull
    private Integer edad;

    @NotNull
    private Double sueldo;

    private String foto;
}
