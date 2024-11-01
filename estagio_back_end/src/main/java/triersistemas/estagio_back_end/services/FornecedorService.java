package triersistemas.estagio_back_end.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

import java.util.List;

public interface FornecedorService {

    FornecedorResponseDto addFornecedor(FornecedorRequestDto requestDto);

    FornecedorResponseDto getFornecedorById(Long id);

    FornecedorResponseDto deleteFornecedor(Long id);

    Page<FornecedorResponseDto> getFornecedorPaged(@Valid String nome, @Valid String cnpj, SituacaoCadastro situacaoCadastro, Pageable of);

    FornecedorResponseDto updateFornecedor(Long id, @Valid FornecedorRequestDto requestDto);

}
