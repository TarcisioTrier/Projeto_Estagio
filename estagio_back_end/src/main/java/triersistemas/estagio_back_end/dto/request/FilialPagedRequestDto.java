package triersistemas.estagio_back_end.dto.request;

import triersistemas.estagio_back_end.dto.EnderecosDto;
import triersistemas.estagio_back_end.enuns.FilialOrderEnum;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;

import java.util.List;

public record FilialPagedRequestDto(
        String nomeFantasia,
        String razaoSocial,
        String cnpj,
        String telefone,
        String email,
        SituacaoContrato situacaoContrato,
        EnderecosDto endereco,
        List<Orderer> order,
        List<Filter> filter
) {
}