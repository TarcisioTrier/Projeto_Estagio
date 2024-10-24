package triersistemas.estagio_back_end.dto.request;

import triersistemas.estagio_back_end.enuns.FornecedorOrderEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

import java.util.List;
import java.util.Map;

public record FornecedorPagedRequestDto(
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String email,
        SituacaoCadastro situacaoCadastro,
        List<Orderer> orderer,
        Map<String, String>  filter

) {
}