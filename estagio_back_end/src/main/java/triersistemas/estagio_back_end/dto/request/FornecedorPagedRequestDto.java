package triersistemas.estagio_back_end.dto.request;

import triersistemas.estagio_back_end.enuns.FornecedorOrderEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

import java.util.List;

public record FornecedorPagedRequestDto(
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String email,
        SituacaoCadastro situacaoCadastro,
        List<Orderer> orderer,
        List<Filter> filter

) {
}