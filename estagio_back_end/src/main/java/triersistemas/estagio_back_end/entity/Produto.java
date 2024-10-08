package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codigo_barras", nullable = false, unique = true)
    private String codigoBarras;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "descricao")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "grupo_produto_id", nullable = false)
    private GrupoProduto grupoProduto;

    @Column(name = "tipo_produto", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @Column(name = "apresentacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private Apresentacao apresentacao;

    @Column(name = "margem_lucro")
    private BigDecimal margemLucro;

    @Column(name = "aceita_atualizacao_preco")
    private Boolean aceitaAtualizacaoPreco;

    @Column(name = "valor_produto", nullable = false)
    private BigDecimal valorProduto;

    @Column(name = "valor_venda", nullable = false)
    private BigDecimal valorVenda;

    @Column(name = "data_ultima_atualizacao_preco")
    private LocalDate dataUltimaAtualizacaoPreco;

    @Column(name = "situacao_cadastro", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoCadastro situacaoCadastro;

    public Produto(ProdutoRequestDto dto, GrupoProduto grupoProduto) {
        this.codigoBarras = dto.codigoBarras();
        this.nome = dto.nome();
        this.descricao = dto.descricao();
        this.grupoProduto = grupoProduto;
        this.tipoProduto = dto.tipoProduto();
        this.apresentacao = dto.apresentacao();
        this.margemLucro = dto.margemLucro();
        this.aceitaAtualizacaoPreco = dto.aceitaAtualizacaoPreco();
        this.valorProduto = dto.valorProduto();
        this.situacaoCadastro = Objects.nonNull(dto.situacaoCadastro()) ? dto.situacaoCadastro() : SituacaoCadastro.ATIVO;;
    }
    public void atualizaProduto(ProdutoRequestDto dto, Optional<GrupoProduto> grupoProduto) {
        this.codigoBarras = Optional.ofNullable(dto.codigoBarras()).orElse(this.codigoBarras);
        this.nome = Optional.ofNullable(dto.nome()).orElse(this.nome);
        this.descricao = Optional.ofNullable(dto.descricao()).orElse(this.descricao);
        this.grupoProduto = grupoProduto.orElse(this.grupoProduto);
        this.tipoProduto = Optional.ofNullable(dto.tipoProduto()).orElse(this.tipoProduto);
        this.apresentacao = Optional.ofNullable(dto.apresentacao()).orElse(this.apresentacao);
        this.margemLucro = Optional.ofNullable(dto.margemLucro()).orElse(this.margemLucro);
        this.aceitaAtualizacaoPreco = Optional.ofNullable(dto.aceitaAtualizacaoPreco()).orElse(this.aceitaAtualizacaoPreco);
        this.valorProduto = Optional.ofNullable(dto.valorProduto()).orElse(this.valorProduto);
        this.situacaoCadastro = Optional.ofNullable(dto.situacaoCadastro()).orElse(this.situacaoCadastro);

    }


    @PrePersist
    @PreUpdate
    private void calculateValorVenda() {
        if (this.margemLucro != null) {
            this.valorVenda = this.valorProduto.add(this.margemLucro);
        }
    }

}