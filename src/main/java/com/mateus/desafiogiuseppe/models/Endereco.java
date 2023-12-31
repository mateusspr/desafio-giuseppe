package com.mateus.desafiogiuseppe.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "enderecos")
@Data
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long idPessoa;
    private String pais;
    private String estado;
    private String bairro;
    private String endereco;
    private String numero;
    private String complemento;
    private String cep;
}
