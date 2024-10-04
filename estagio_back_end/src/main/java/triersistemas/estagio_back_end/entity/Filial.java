package triersistemas.estagio_back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.FilialRequestDto;
import triersistemas.estagio_back_end.enums.SituacaoContrato;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Filiais")
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome_fantasia", nullable = false)
    private String nomeFantasia;

    @Column(name = "razao_social", nullable = false)
    private String razaoSocial;

    @Column(name = "cnpj", nullable = false, unique = true)
    private String cnpj;

    @Column(name = "telefone", nullable = false)
    private String telefone;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "situacao_contratos", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoContrato situacaoContrato;

    @JsonIgnore
    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Fornecedor> fornecedores;

    @JsonIgnore
    @OneToMany(mappedBy = "filial", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<GrupoProduto> grupoProdutos;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "endereco_id", referencedColumnName = "id")
    private Endereco endereco;

    public Filial(FilialRequestDto dto) {
        this.nomeFantasia = dto.nomeFantasia();
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();
        this.telefone = dto.telefone();
        this.email = dto.email();
        this.situacaoContrato = dto.situacaoContrato();
        if (dto.rua() != null && dto.cidade() != null && dto.estado() != null) {
            this.endereco = new Endereco(
                    dto.rua(),
                    dto.numero(),
                    dto.complemento(),
                    dto.cidade(),
                    dto.estado(),
                    dto.cep()
            );
        }
    }
}
