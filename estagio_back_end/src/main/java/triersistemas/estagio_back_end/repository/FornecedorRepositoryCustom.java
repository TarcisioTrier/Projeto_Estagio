package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import triersistemas.estagio_back_end.dto.request.FornecedorPagedRequestDto;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;

import java.util.List;

public interface FornecedorRepositoryCustom {

   List<FornecedorResponseDto> buscarFornecedores(String nome, Long filialId);

    Page<FornecedorResponseDto> buscarFornecedores(Long filialId, FornecedorPagedRequestDto fornecedorDto, PageRequest of);
}
