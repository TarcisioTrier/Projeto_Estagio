package triersistemas.estagio_back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import triersistemas.estagio_back_end.dto.request.FornecedorRequestDto;
import triersistemas.estagio_back_end.enuns.SituacaoCadastro;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "Fornecedores")
public class Fornecedor {

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

    @Column(name = "situacao_cadastro", nullable = false)
    @Enumerated(EnumType.STRING)
    private SituacaoCadastro situacaoCadastro;

    @ManyToOne
    @JoinColumn(name = "filial_id")
    private Filial filial;

    public  Fornecedor(FornecedorRequestDto dto, Filial filial){
        this.nomeFantasia = dto.nomeFantasia();
        this.razaoSocial = dto.razaoSocial();
        this.cnpj = dto.cnpj();
        this.telefone = dto.telefone();
        this.email = dto.email();
        this.situacaoCadastro = SituacaoCadastro.ATIVO;
        this.filial = filial;
    }
}
