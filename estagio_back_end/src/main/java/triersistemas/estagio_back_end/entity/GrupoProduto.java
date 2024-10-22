package triersistemas.estagio_back_end.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "GruposProdutos")
public class GrupoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Nome do grupo é obrigatório")
    @Column(name = "nome_grupo", nullable = false)
    private String nomeGrupo;

    @NotNull(message = "Tipo de grupo é obrigatório")
    @Column(name = "tipo_grupo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoGrupoProduto tipoGrupo;

    @DecimalMin(value = "0.0", message = "Valor Venda não pode ser negativo")
    @NotNull(message = "Margem de lucro é obrigatória")
    @Column(name = "margem_lucro", nullable = false)
    private BigDecimal margemLucro;

    @NotNull(message = "Situação do cadastro é obrigatória")
    @Column(name = "atualiza_preco")
    private Boolean atualizaPreco;

    @Column(name = "situacao_cadastro", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoCadastro situacaoCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "grupoProduto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Produto> produtos;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;

    public GrupoProduto(GrupoProdutoRequestDto dto, Filial filial) {
        this.nomeGrupo = dto.nomeGrupo();
        this.tipoGrupo = dto.tipoGrupo();
        this.margemLucro = dto.margemLucro();
        this.atualizaPreco = dto.atualizaPreco();
        this.situacaoCadastro = Objects.nonNull(dto.situacaoCadastro()) ? dto.situacaoCadastro() : SituacaoCadastro.ATIVO;;
        this.filial = filial;
    }


    public void alteraGrupoProduto(GrupoProdutoRequestDto grupoProdutoRequestDto, Optional<Filial> filial) {
        Optional.ofNullable(grupoProdutoRequestDto.nomeGrupo()).ifPresent(this::setNomeGrupo);
        Optional.ofNullable(grupoProdutoRequestDto.tipoGrupo()).ifPresent(this::setTipoGrupo);
        Optional.ofNullable(grupoProdutoRequestDto.margemLucro()).ifPresent(this::setMargemLucro);
        Optional.ofNullable(grupoProdutoRequestDto.atualizaPreco()).ifPresent(this::setAtualizaPreco);
        Optional.ofNullable(grupoProdutoRequestDto.situacaoCadastro()).ifPresent(this::setSituacaoCadastro);
        filial.ifPresent(this::setFilial);
        atualizarProdutos();
    }

    private void atualizarProdutos() {
        if (produtos != null) {
            produtos.forEach(Produto::calculateValorVenda);
        }
    }
}
