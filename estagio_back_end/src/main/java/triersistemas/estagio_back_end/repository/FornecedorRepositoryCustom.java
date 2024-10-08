package triersistemas.estagio_back_end.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import triersistemas.estagio_back_end.dto.response.FornecedorResponseDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

public interface FornecedorRepositoryCustom {

   Page<FornecedorResponseDto> buscarFornecedores(String nome, String cnpj, SituacaoCadastro situacaoCadastro, Pageable of);
}
