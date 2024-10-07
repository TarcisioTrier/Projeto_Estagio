package triersistemas.estagio_back_end.dto.response;

import triersistemas.estagio_back_end.entity.GrupoProduto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.math.BigDecimal;

public record GrupoProdutoResponseDto(
        Long id,
        String nomeGrupo,
        TipoGrupoProduto tipoGrupo,
        BigDecimal margemLucro,
        Boolean atualizaPreco,
        SituacaoCadastro situacaoCadastro,
        Long filialId
) {
    public GrupoProdutoResponseDto(GrupoProduto grupoProduto) {
        this(
                grupoProduto.getId(),
                grupoProduto.getNomeGrupo(),
                grupoProduto.getTipoGrupo(),
                grupoProduto.getMargemLucro(),
                grupoProduto.getAtualizaPreco(),
                grupoProduto.getSituacaoCadastro(),
                grupoProduto.getFilial().getId()
        );
    }
}
