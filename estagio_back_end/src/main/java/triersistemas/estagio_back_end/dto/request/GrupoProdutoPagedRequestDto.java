package triersistemas.estagio_back_end.dto.request;

import triersistemas.estagio_back_end.enuns.GrupoProdutoOrderEnum;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public record GrupoProdutoPagedRequestDto(
        String nomeGrupo,
        TipoGrupoProduto tipoGrupo,
        BigDecimal margemLucro,
        Boolean atualizaPreco,
        SituacaoCadastro situacaoCadastro,
        List<Orderer> orderer,
        Map<String, String> filter
) {
}
