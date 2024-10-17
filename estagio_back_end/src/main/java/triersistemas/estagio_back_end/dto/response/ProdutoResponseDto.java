package triersistemas.estagio_back_end.dto.response;

import triersistemas.estagio_back_end.entity.Produto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;

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
        Boolean atualizaPreco,
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
                produto.getAtualizaPreco(),
                produto.getValorProduto(),
                produto.getValorVenda(),
                produto.getDataUltimaAtualizacaoPreco(),
                produto.getSituacaoCadastro()
        );
    }
}
