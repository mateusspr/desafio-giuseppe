package com.mateus.desafiogiuseppe.services;

import com.mateus.desafiogiuseppe.dto.EnderecoDto;
import com.mateus.desafiogiuseppe.dto.PessoaDetalhesDto;
import com.mateus.desafiogiuseppe.dto.PessoaDto;
import com.mateus.desafiogiuseppe.models.Pessoa;
import com.mateus.desafiogiuseppe.repositories.PessoaRepository;
import com.mateus.desafiogiuseppe.stubs.EnderecoStub;
import com.mateus.desafiogiuseppe.stubs.PessoaStub;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private EnderecoService enderecoService;



    @Test
    @DisplayName(" Retorna todas as pessos do banco")
    void findAllPessoas() {
        when(pessoaRepository.findAll()).thenReturn(List.of(PessoaStub.criandoPessoa()));

        pessoaService.findAllPessoas();

        verify(pessoaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Salva pessoa no banco e verifica se esse valor não é nulo")
    void savePessoa() {
        PessoaDto pessoaDto = PessoaStub.criandoPessoaDto();
        Pessoa pessoa = PessoaStub.criandoPessoa();

        when(pessoaRepository.save(any())).thenReturn(pessoa);

        var pessoaSalva = pessoaService.savePessoa(pessoaDto);

        verify(pessoaRepository, times(1)).save(any());

        assertNotNull(pessoaSalva);
    }

    @Test
    @DisplayName("Pesquisa pessoa por id")
    void findPessoaById() {
        Long id = 1L;
        Pessoa pessoa = PessoaStub.criandoPessoa();

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        PessoaDto foundPessoa = pessoaService.findPessoaById(id);

        verify(pessoaRepository, times(1)).findById(id);
        assertNotNull(foundPessoa);
    }

    @Test
    @DisplayName("Erro  ao buscar id caso pessoa(id) não seja encontrado")
    void findPessoaById_PessoaNaoEncontrada() {
        Long id = 2L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pessoaService.findPessoaById(id));

        verify(pessoaRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Erro ao buscar detalhes de pessoa  pelo id")
    void detalhesPessoa_PessoaNaoEncontrada() {
        Long id = 2L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pessoaService.detalhesPessoa(id));

        verify(pessoaRepository, times(1)).findById(id);
        verifyNoInteractions(enderecoService);
    }

    @Test
    @DisplayName("Detalhes da pessoa, incluindo enderecos")
    void detalhesPessoa() {
        Long id = 1L;
        Pessoa pessoa = PessoaStub.criandoPessoa();
        List<EnderecoDto> enderecoDtoList = List.of(EnderecoStub.criandoEnderecoDto());

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));
        when(enderecoService.listarEnderecosPessoa(id)).thenReturn(enderecoDtoList);

        PessoaDetalhesDto detalhesDto = pessoaService.detalhesPessoa(id);

        verify(pessoaRepository, times(1)).findById(id);
        verify(enderecoService, times(1)).listarEnderecosPessoa(id);
        assertNotNull(detalhesDto);
    }

    @Test
    @DisplayName("Deleta pessoa por id")
    void deletePessoa() {
        Long id = 1L;
        Pessoa pessoa = PessoaStub.criandoPessoa();

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoa));

        pessoaService.deletePessoa(id);

        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, times(1)).deleteById(id);
    }

    @Test
    @DisplayName("Quando o id fa pessoa nao é encontrada, retorna a Exception")
    void deletePessoa_PessoaNaoEncontrada() {
        Long id = 10L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> pessoaService.deletePessoa(id));

        verify(pessoaRepository, times(1)).findById(id);
        verify(pessoaRepository, never()).deleteById(id);
    }
}