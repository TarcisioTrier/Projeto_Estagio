package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.ProdutoRequestDto;
import triersistemas.estagio_back_end.dto.response.ProdutoResponseDto;
import triersistemas.estagio_back_end.enuns.Apresentacao;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoProduto;
import triersistemas.estagio_back_end.exceptions.InvalidMargemException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;

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
        Optional.ofNullable(dto.codigoBarras()).ifPresent(this::setCodigoBarras);
        Optional.ofNullable(dto.nome()).ifPresent(this::setNome);
        Optional.ofNullable(dto.descricao()).ifPresent(this::setDescricao);
        grupoProduto.ifPresent(this::setGrupoProduto);
        Optional.ofNullable(dto.tipoProduto()).ifPresent(this::setTipoProduto);
        Optional.ofNullable(dto.apresentacao()).ifPresent(this::setApresentacao);
        Optional.ofNullable(dto.margemLucro()).ifPresent(this::setMargemLucro);
        Optional.ofNullable(dto.atualizaPreco()).ifPresent(this::setAtualizaPreco);
        Optional.ofNullable(dto.valorProduto()).ifPresent(this::setValorProduto);
        Optional.ofNullable(dto.situacaoCadastro()).ifPresent(this::setSituacaoCadastro);
        calculateValorVenda();
    }

    @PrePersist
    public void calculateValorVenda() {
        BigDecimal margemLucroEfetiva = Optional.ofNullable(this.margemLucro)
                .orElse(this.grupoProduto.getMargemLucro());
        margemLucroEfetiva = margemLucroEfetiva.divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_EVEN);

        this.valorVenda = this.valorProduto.divide(BigDecimal.ONE.subtract(margemLucroEfetiva), 2, RoundingMode.HALF_EVEN);
        dataUltimaAtualizacaoPreco = LocalDate.now();
    }

    public void calculateMargemLucro(){
        var margemLucro = this.valorVenda.subtract(this.valorProduto).divide(this.valorVenda, 2, RoundingMode.HALF_EVEN).multiply(BigDecimal.valueOf(100));
        if (margemLucro.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidMargemException("Valor da Margem não pode ser negativo");
        }
        if (margemLucro.compareTo(BigDecimal.valueOf(99.99)) >= 0) {
            throw new InvalidMargemException("Valor da Margem não pode ser maior que ou igual a 100%");
        }else{
            this.margemLucro = margemLucro;
        }
    }

}