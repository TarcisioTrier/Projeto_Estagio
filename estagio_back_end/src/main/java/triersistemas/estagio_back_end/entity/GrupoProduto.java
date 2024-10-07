package triersistemas.estagio_back_end.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.GrupoProdutoRequestDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;
import triersistemas.estagio_back_end.enuns.TipoGrupoProduto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "GruposProdutos")
public class GrupoProduto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_grupo", nullable = false)
    private String nomeGrupo;

    @Column(name = "tipo_grupo", nullable = false)
    @Enumerated(EnumType.STRING)
    private TipoGrupoProduto tipoGrupo;

    @Column(name = "margem_lucro", nullable = false)
    private BigDecimal margemLucro;

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


}
