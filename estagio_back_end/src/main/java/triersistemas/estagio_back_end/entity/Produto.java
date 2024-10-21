package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "Produtos",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"codigo_barras", "filial_id"})}
)
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Código de Barras é obrigatório")
    @Column(name = "codigo_barras", nullable = false)
    private String codigoBarras;

    @NotBlank(message = "Nome é obrigatório")
    @Column(name = "nome", nullable = false)
    private String nome;
    
    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "grupo_produto_id", nullable = false)
    private GrupoProduto grupoProduto;

    @ManyToOne
    @JoinColumn(name = "filial_id", nullable = false)
    private Filial filial;

    @NotNull(message = "Tipo de Produto é obrigatório")
    @Column(name = "tipo_produto", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoProduto tipoProduto;

    @NotNull(message = "Apresentação é obrigatória")
    @Column(name = "apresentacao", nullable = false)
    @Enumerated(EnumType.STRING)
    private Apresentacao apresentacao;

    @DecimalMin(value = "0.0", message = "Margem de Lucro não pode ser negativa")
    @Column(name = "margem_lucro")
    private BigDecimal margemLucro;

    @Column(name = "atualizaPreco")
    private Boolean atualizaPreco;

    @Column(name = "valor_produto", nullable = false)
    private BigDecimal valorProduto;

    @NotNull(message = "Valor Venda é obrigatório")
    @DecimalMin(value = "0.0", message = "Valor Venda não pode ser negativo")
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
        this.atualizaPreco = dto.atualizaPreco();
        this.valorProduto = dto.valorProduto();
        this.filial = grupoProduto.getFilial();
        this.situacaoCadastro = Objects.nonNull(dto.situacaoCadastro()) ? dto.situacaoCadastro() : SituacaoCadastro.ATIVO;
        ;
    }

    public void atualizaProduto(ProdutoRequestDto dto, Optional<GrupoProduto> grupoProduto) {
        this.codigoBarras = Optional.ofNullable(dto.codigoBarras()).orElse(this.codigoBarras);
        this.nome = Optional.ofNullable(dto.nome()).orElse(this.nome);
        this.descricao = Optional.ofNullable(dto.descricao()).orElse(this.descricao);
        this.grupoProduto = grupoProduto.orElse(this.grupoProduto);
        this.tipoProduto = Optional.ofNullable(dto.tipoProduto()).orElse(this.tipoProduto);
        this.apresentacao = Optional.ofNullable(dto.apresentacao()).orElse(this.apresentacao);
        this.margemLucro = Optional.ofNullable(dto.margemLucro()).orElse(this.margemLucro);
        this.atualizaPreco = Optional.ofNullable(dto.atualizaPreco()).orElse(this.atualizaPreco);
        this.valorProduto = Optional.ofNullable(dto.valorProduto()).orElse(this.valorProduto);
        this.situacaoCadastro = Optional.ofNullable(dto.situacaoCadastro()).orElse(this.situacaoCadastro);
        calculateValorVenda();
    }

    @PrePersist
    public void calculateValorVenda() {
        BigDecimal margemLucroEfetiva = Optional.ofNullable(this.margemLucro)
                .orElse(this.grupoProduto.getMargemLucro());
        margemLucroEfetiva = margemLucroEfetiva.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);

        this.valorVenda = this.valorProduto.divide(
                BigDecimal.ONE.subtract(margemLucroEfetiva));
        dataUltimaAtualizacaoPreco = LocalDate.now();
    }

    public void calculateValorProduto(){
        BigDecimal margemLucroEfetiva = Optional.ofNullable(this.margemLucro)
                .orElse(this.grupoProduto.getMargemLucro());
        margemLucroEfetiva = margemLucroEfetiva.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);

        this.valorProduto = this.valorVenda.subtract(this.valorVenda.multiply(margemLucroEfetiva));
        dataUltimaAtualizacaoPreco = LocalDate.now();
    }

}