package triersistemas.estagio_back_end.services;

import triersistemas.estagio_back_end.dto.AtualizaPrecoDto;
import triersistemas.estagio_back_end.dto.response.GrupoProdutoResponseDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;

import java.util.List;

public interface AtualizaPrecoService {
    List<ProdutoResponseDto> alteraMargemProduto(AtualizaPrecoDto atualizaPreco);

    List<GrupoProdutoResponseDto> alteraMargemGrupoProduto(AtualizaPrecoDto atualizaProduto);
}
