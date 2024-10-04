package triersistemas.estagio_back_end.dto;

import triersistemas.estagio_back_end.entity.Fornecedor;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

public record FornecedorResponseDto(Long id, String nomeFantasia, String razaoSocial, String cnpj, String telefone,
                                    String email, SituacaoCadastro situacaoCadastro, Long filialId) {


    public FornecedorResponseDto(Fornecedor fornecedor) {
        this(
                fornecedor.getId(),
                fornecedor.getNomeFantasia(),
                fornecedor.getRazaoSocial(),
                fornecedor.getCnpj(),
                fornecedor.getTelefone(),
                fornecedor.getEmail(),
                fornecedor.getSituacaoCadastro(),
                fornecedor.getFilial().getId());
    }
}