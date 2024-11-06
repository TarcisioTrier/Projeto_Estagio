package triersistemas.estagio_back_end.dto.response;

import triersistemas.estagio_back_end.entity.Filial;

public record FilialChartDto(
        String nomeFantasia,
        Integer produtos,
        Integer gruposProduto,
        Integer fornecedores,
        ProdutoResponseDto maiorValorVenda,
        ProdutoResponseDto maiorValorProduto

) {
    public FilialChartDto(Filial filial) {
        this(
                filial.getNomeFantasia(),
                filial.getProdutos().size(),
                filial.getGrupoProdutos().size(),
                filial.getFornecedores().size(),
                filial.getProdutos().stream().max((p1, p2) -> p1.getValorVenda().compareTo(p2.getValorVenda())).map(ProdutoResponseDto::new).orElse(null),
                filial.getProdutos().stream().max((p1,p2)-> p1.getValorProduto().compareTo(p2.getValorProduto())).map(ProdutoResponseDto::new).orElse(null)
        );
    }

}
