package triersistemas.estagio_back_end.dto.response;

import triersistemas.estagio_back_end.entity.GrupoProduto;

public record GrupoProdutoChartDto(
        String nomeGrupo,
        Integer produtos
) {
    public GrupoProdutoChartDto(GrupoProduto grupoProduto) {
        this(
                grupoProduto.getNomeGrupo(),
                grupoProduto.getProdutos().size()
        );
    }

}
