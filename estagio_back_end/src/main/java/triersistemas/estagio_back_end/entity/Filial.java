package triersistemas.estagio_back_end.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.FilialRequestDto;
import triersistemas.estagio_back_end.enuns.SituacaoContrato;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private Enderecos endereco;

    public Filial(FilialRequestDto dto) {
        this.nomeFantasia = dto.nomeFantasia();
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();
        this.telefone = dto.telefone();
        this.email = dto.email();
        this.situacaoContrato = Objects.nonNull(dto.situacaoContrato()) ? dto.situacaoContrato() : SituacaoContrato.ATIVO;
        if (dto.endereco() != null) {
            this.endereco = new Enderecos(
                    dto.endereco().logradouro(),
                    dto.endereco().numero(),
                    dto.endereco().complemento(),
                    dto.endereco().localidade(),
                    dto.endereco().estado(),
                    dto.endereco().cep(),
                    dto.endereco().bairro()
            );
        }
    }

    public void alterarDados(FilialRequestDto dto) {

        Optional.ofNullable(dto.nomeFantasia()).ifPresent(this::setNomeFantasia);
        Optional.ofNullable(dto.razaoSocial()).ifPresent(this::setRazaoSocial);
        Optional.ofNullable(dto.cnpj()).ifPresent(this::setCnpj);
        Optional.ofNullable(dto.telefone()).ifPresent(this::setTelefone);
        Optional.ofNullable(dto.email()).ifPresent(this::setEmail);
        Optional.ofNullable(dto.situacaoContrato()).ifPresent(this::setSituacaoContrato);

        Optional.ofNullable(dto.endereco()).ifPresent(enderecoDto -> {
            if (this.endereco == null) {
                this.endereco = new Enderecos();
            }
            this.endereco.alterarDados(enderecoDto);
        });
    }
}
