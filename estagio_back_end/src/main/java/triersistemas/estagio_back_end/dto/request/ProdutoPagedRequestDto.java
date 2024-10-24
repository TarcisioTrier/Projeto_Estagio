package triersistemas.estagio_back_end.dto.request;

import triersistemas.estagio_back_end.enuns.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProdutoPagedRequestDto(

        String codigoBarras,
        String nome,
        String descricao,
        TipoProduto tipoProduto,
        Apresentacao apresentacao,
        BigDecimal margemLucro,
        Boolean atualizaPreco,
        BigDecimal valorProduto,
        BigDecimal valorVenda,
        LocalDate dataUltimaAtualizacaoPreco,
        SituacaoCadastro situacaoCadastro,
        List<Orderer> orderer

) {
}