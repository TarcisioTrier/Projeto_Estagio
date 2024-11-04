package triersistemas.estagio_back_end.services;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;

import java.util.List;

public interface FornecedorService {

    FornecedorResponseDto addFornecedor(FornecedorRequestDto requestDto);

    FornecedorResponseDto getFornecedorById(Long id);

    FornecedorResponseDto deleteFornecedor(Long id);

    FornecedorResponseDto updateFornecedor(Long id, @Valid FornecedorRequestDto requestDto);

    Page<FornecedorResponseDto> getFornecedorPaged(@Valid Long filialId, FornecedorPagedRequestDto fornecedorDto, PageRequest of);
}
