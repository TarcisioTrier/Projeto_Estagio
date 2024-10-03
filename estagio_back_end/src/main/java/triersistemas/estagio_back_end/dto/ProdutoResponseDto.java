package triersistemas.estagio_back_end.dto;

import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enums.Apresentacao;
import triersistemas.estagio_back_end.enums.SituacaoCadastro;
import triersistemas.estagio_back_end.enums.TipoProduto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoResponseDto(
        Long id,
        String codigoBarras,
        String nome,
        String descricao,
        Long grupoProdutoId,
        TipoProduto tipoProduto,
        Apresentacao apresentacao,
        BigDecimal margemLucro,
        Boolean aceitaAtualizacaoPreco,
        BigDecimal valorProduto,
        BigDecimal valorVenda,
        LocalDate dataUltimaAtualizacaoPreco,
        SituacaoCadastro situacaoCadastro
) {
    public ProdutoResponseDto(Produto produto) {
        this(
                produto.getId(),
                produto.getCodigoBarras(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getGrupoProduto().getId(),
                produto.getTipoProduto(),
                produto.getApresentacao(),
                produto.getMargemLucro(),
                produto.getAceitaAtualizacaoPreco(),
                produto.getValorProduto(),
                produto.getValorVenda(),
                produto.getDataUltimaAtualizacaoPreco(),
                produto.getSituacaoCadastro()
        );
    }
}
